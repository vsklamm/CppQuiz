package com.vsklamm.cppquiz.data.model;

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
    public Integer difficulty;

    @SerializedName("result")
    @Expose
    @TypeConverters(ResultBehaviourType.class)
    public ResultBehaviourType result;

    @SerializedName("answer")
    @Expose
    public String answer;

    @SerializedName("question")
    @Expose
    public String code;

    @SerializedName("hint")
    @Expose
    public String hint;

    @SerializedName("explanation")
    @Expose
    public String explanation;

}
