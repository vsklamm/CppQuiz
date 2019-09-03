package com.vsklamm.cppquiz.data.model;

import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vsklamm.cppquiz.utils.ResultBehaviourType;

import java.io.Serializable;

public class Question implements Serializable {

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
}
