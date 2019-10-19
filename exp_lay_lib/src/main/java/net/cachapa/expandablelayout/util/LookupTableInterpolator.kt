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

package net.cachapa.expandablelayout.util

import android.view.animation.Interpolator
import kotlin.math.min

abstract class LookupTableInterpolator(values: FloatArray) : Interpolator {

    private val mValues: FloatArray = values
    private val mStepSize: Float = 1f / (values.size - 1)

    override fun getInterpolation(input: Float): Float {
        if (input >= 1.0f) return 1.0f
        if (input <= 0f) return 0f

        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        val position = min((input * (mValues.size - 1)).toInt(), mValues.size - 2)

        // Calculate values to account for small offsets as the lookup table has discrete values
        val quantized = position * mStepSize
        val diff = input - quantized
        val weight = diff / mStepSize

        // Linearly interpolate between the table values
        return mValues[position] + weight * (mValues[position + 1] - mValues[position])
    }
}
