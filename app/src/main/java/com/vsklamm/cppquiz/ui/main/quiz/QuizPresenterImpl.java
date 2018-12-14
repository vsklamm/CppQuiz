package com.vsklamm.cppquiz.ui.main.quiz;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.ui.main.GamePresenter;

public class QuizPresenterImpl implements GamePresenter {

    @NonNull
    private final Activity activity;

    @NonNull
    private final QuizView mQuizView;

    public QuizPresenterImpl(@NonNull Activity activity, @NonNull QuizView quizView) {
        this.activity = activity;
        this.mQuizView = quizView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void nextQuestion() {

    }

    @Override
    public void getHint() {

    }

    @Override
    public boolean checkAnswer() {
        return false;
    }

    @Override
    public void giveUp() {

    }
}