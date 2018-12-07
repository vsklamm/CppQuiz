package com.vsklamm.cppquiz.ui.main.quiz;

import android.app.Activity;

import com.vsklamm.cppquiz.ui.main.GamePresenter;

public class QuizPresenterImpl implements GamePresenter {

    Activity activity;
    QuizView view;

    public QuizPresenterImpl(Activity activity, QuizView view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
