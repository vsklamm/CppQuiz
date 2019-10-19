package com.vsklamm.cppquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PublishedDatabase {

    @SerializedName("version")
    @Expose
    private int dumpVersion;

    @SerializedName("cpp_standard")
    @Expose
    private String cppStandard;

    @SerializedName("questions")
    @Expose
    private List<Question> questions;

    public int getDumpVersion() {
        return dumpVersion;
    }

    public void setDumpVersion(int dumpVersion) {
        this.dumpVersion = dumpVersion;
    }

    public String getCppStandard() {
        return cppStandard;
    }

    public void setCppStandard(String cppStandard) {
        this.cppStandard = cppStandard;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
