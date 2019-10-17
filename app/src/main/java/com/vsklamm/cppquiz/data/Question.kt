package com.vsklamm.cppquiz.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vsklamm.cppquiz.utils.ResultBehaviourType

import java.io.Serializable

@Entity
class Question(@field:PrimaryKey
               val id: Int,
               var difficulty: Int,
               @field:TypeConverters(ResultBehaviourType::class)
               var result: ResultBehaviourType,
               var answer: String,
               var code: String,
               var hint: String,
               var explanation: String) : Serializable {

    @Ignore
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Question) return false
        val otherQuestion = other as Question?
        return (this.id == otherQuestion!!.id
                && this.difficulty == otherQuestion.difficulty
                && this.result == otherQuestion.result
                && this.answer == otherQuestion.answer
                && this.code == otherQuestion.code
                && this.hint == otherQuestion.hint
                && this.explanation == otherQuestion.explanation)
    }

    @Ignore
    fun compareWithAnswer(usersAnswer: UsersAnswer): Boolean {
        return (this.id == usersAnswer.questionId && this.result == usersAnswer.result
                && (this.result != ResultBehaviourType.OK || this.answer == usersAnswer.answer))
    }
}
