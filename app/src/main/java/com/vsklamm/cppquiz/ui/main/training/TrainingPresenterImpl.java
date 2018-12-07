package com.vsklamm.cppquiz.ui.main.training;

import android.app.Activity;

import com.vsklamm.cppquiz.ui.main.GamePresenter;

public class TrainingPresenterImpl implements GamePresenter {

    Activity activity;
    TrainingView view;

    public TrainingPresenterImpl(Activity activity, TrainingView view) {
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
