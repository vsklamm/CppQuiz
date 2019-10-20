package com.vsklamm.cppquiz.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vsklamm.cppquiz.utils.ResultBehaviourType;

import java.io.Serializable;

@Entity
public class Question implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("difficulty")
    @Expose
    private Integer difficulty;

    @SerializedName("result")
    @Expose
    @TypeConverters(ResultBehaviourType.class)
    private ResultBehaviourType result;

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("question")
    @Expose
    private String code;

    @SerializedName("hint")
    @Expose
    private String hint;

    @SerializedName("explanation")
    @Expose
    private String explanation;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public ResultBehaviourType getResult() {
        return result;
    }

    public void setResult(ResultBehaviourType result) {
        this.result = result;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Ignore
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Question)) return false;
        Question otherQuestion = (Question) other;
        return (this.id.equals(otherQuestion.id)
                && this.difficulty.equals(otherQuestion.difficulty)
                && this.result == otherQuestion.result
                && this.answer.equals(otherQuestion.answer)
                && this.code.equals(otherQuestion.code)
                && this.hint.equals(otherQuestion.hint)
                && this.explanation.equals(otherQuestion.explanation));
    }

    @Ignore
    public boolean compareWithAnswer(UsersAnswer usersAnswer) {
        return (this.id == usersAnswer.questionId && this.result == usersAnswer.result
                && (this.result != ResultBehaviourType.OK || this.answer.equals(usersAnswer.answer)));
    }
}
