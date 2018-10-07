package com.vsklamm.cppquiz.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class UsersAnswer implements Serializable {

    public final int questionId;

    @NonNull
    public final ResultBehaviourType result;

    @NonNull
    public final String answer;

    /*
    @NonNull
    public final Date dateTime;
    */

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
