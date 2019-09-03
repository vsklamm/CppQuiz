package com.vsklamm.cppquiz.data.model;

import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.utils.ResultBehaviourType;

import java.io.Serializable;

public class UsersAnswer implements Serializable {

    public final int questionId;

    @NonNull
    public final ResultBehaviourType result;

    @NonNull
    public final String answer;

    public UsersAnswer(int questionId) {
        this(questionId, ResultBehaviourType.OK, "");
    }

    public UsersAnswer(int questionId,
                       @NonNull ResultBehaviourType result,
                       @NonNull String answer) {
        this.questionId = questionId;
        this.result = result;
        this.answer = answer;
    }
}
