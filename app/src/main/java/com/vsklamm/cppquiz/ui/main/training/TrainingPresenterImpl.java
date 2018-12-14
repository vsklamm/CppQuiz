package com.vsklamm.cppquiz.ui.main.training;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.ui.main.GamePresenter;

public class TrainingPresenterImpl implements GamePresenter {

    @NonNull
    private final Activity activity;

    @NonNull
    private final TrainingView mTrainingView;

    public TrainingPresenterImpl(@NonNull Activity activity, @NonNull TrainingView trainingView) {
        this.activity = activity;
        this.mTrainingView = trainingView;

        mTrainingView.setPresenter(this);
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