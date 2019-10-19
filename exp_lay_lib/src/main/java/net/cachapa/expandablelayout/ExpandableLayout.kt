/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cachapa.expandablelayout

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import net.cachapa.expandablelayout.util.FastOutSlowInInterpolator
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class ExpandableLayout (context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    interface State {
        companion object {
            const val COLLAPSED = 0
            const val COLLAPSING = 1
            const val EXPANDING = 2
            const val EXPANDED = 3
        }
    }

    private var duration = DEFAULT_DURATION
    private var parallax = 0f
    private var expansion = 0f
    private var orientation = 0
    private var state = 0

    private var interpolator: Interpolator = FastOutSlowInInterpolator
    private var animator: ValueAnimator? = null
    private var listener: OnExpansionUpdateListener? = null

    init {
        if (attrs != null) {
            val atttributes: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            duration = atttributes.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION)
            expansion = if (atttributes.getBoolean(R.styleable.ExpandableLayout_el_expanded, false)) 1f else 0.toFloat()
            orientation = atttributes.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL)
            parallax = atttributes.getFloat(R.styleable.ExpandableLayout_el_parallax, 1f)
            atttributes.recycle()

            state = if (expansion == 0f) State.COLLAPSED else State.EXPANDED
            setParallax(parallax)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        expansion = if (isExpanded()) 1f else 0.toFloat()
        return Bundle().apply {
            putFloat(KEY_EXPANSION, expansion)
            putParcelable(KEY_SUPER_STATE, superState)
        }
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        state = if (expansion == 1f) State.EXPANDED else State.COLLAPSED
        val superState = bundle.getParcelable<Parcelable?>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight

        val size = if (orientation == LinearLayout.HORIZONTAL) width else height
        visibility = if (expansion == 0f && size == 0) View.GONE else View.VISIBLE

        val expansionDelta = size - round(size * expansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child: View = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }
        if (orientation == HORIZONTAL) setMeasuredDimension((width - expansionDelta).toInt(), height)
        else setMeasuredDimension(width, (height - expansionDelta).toInt())
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        animator?.cancel()
        super.onConfigurationChanged(newConfig)
    }

    fun getState(): Int = state

    fun isExpanded(): Boolean = (state == State.EXPANDING || state == State.EXPANDED)

    fun toggle(animate: Boolean = true) {
        if (isExpanded()) collapse(animate) else expand(animate)
    }

    fun expand(animate: Boolean = true) = setExpanded(true, animate)

    fun collapse(animate: Boolean = true) = setExpanded(false, animate)

    fun setExpanded(expand: Boolean, animate: Boolean = true) {
        if (expand == isExpanded()) return

        val targetExpansion = if (expand) 1 else 0
        if (animate) animateSize(targetExpansion)
        else setExpansion(targetExpansion.toFloat())
    }

    fun getDuration(): Int = duration

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    fun getExpansion(): Float = expansion

    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) return

        // Infer state from previous value

        val delta = expansion - this.expansion
        state = when (expansion) {
            0f -> State.COLLAPSED
            1f -> State.EXPANDED
            else -> {
                when {
                    (delta < 0) -> State.COLLAPSING
                    (delta > 0) ->  State.EXPANDING
                    else -> State.COLLAPSED
                }
            }
        }

        visibility = if (state == State.COLLAPSED) View.GONE else View.VISIBLE
        this.expansion = expansion
        requestLayout()
        listener?.onExpansionUpdate(expansion, state)
    }

    fun getParallax(): Float = parallax

    // Make sure parallax is between 0 and 1
    fun setParallax(parallax: Float) {
        var p = parallax
        p = min(1f, max(0f, p))
        this.parallax = p
    }

    fun getOrientation(): Int = orientation

    fun setOrientation(orientation: Int) {
        require(!(orientation < 0 || orientation > 1)) {
            "Orientation must be either 0 (horizontal) or 1 (vertical)"
        }
        this.orientation = orientation
    }

    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener?) {
        this.listener = listener
    }

    private fun animateSize(targetExpansion: Int) {
        if (animator != null) {
            animator?.cancel()
            animator = null
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion.toFloat())
        animator?.interpolator = interpolator
        animator?.duration = duration.toLong()
        animator?.addUpdateListener {
             setExpansion(it.animatedValue as Float)
        }
        animator?.addListener(ExpansionListener(targetExpansion))
        animator?.start()
    }

    interface OnExpansionUpdateListener {
        /**
         * Callback for expansion updates
         *
         * @param expansionFraction Value between 0 (collapsed) and 1 (expanded) representing the the expansion progress
         * @param state             One of [State] representing the current expansion state
         */
        fun onExpansionUpdate(expansionFraction: Float, state: Int)
    }

    private inner class ExpansionListener(private val targetExpansion: Int) : AnimatorListener {
        private var canceled = false

        override fun onAnimationStart(animation: Animator?) {
            state = if (targetExpansion == 0) State.COLLAPSING else State.EXPANDING
        }

        override fun onAnimationEnd(animation: Animator?) {
            if (!canceled) {
                state = if (targetExpansion == 0) State.COLLAPSED else State.EXPANDED
                setExpansion(targetExpansion.toFloat())
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator?) {}

    }

    companion object {
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANSION = "expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val DEFAULT_DURATION = 300
    }
}
