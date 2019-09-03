package com.vsklamm.cppquiz.ui.main;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.ui.main.quiz.QuizPresenterImpl;
import com.vsklamm.cppquiz.ui.main.training.TrainingPresenterImpl;

import static com.vsklamm.cppquiz.ui.main.MainActivity.QUIZ_MODE;
import static com.vsklamm.cppquiz.ui.main.MainActivity.TRAINING_MODE;

public class GameModeFactory {
    public static GamePresenter getPresenter(Activity activity, BaseView view, @NonNull String mode) {
        switch (mode) {
            case TRAINING_MODE:
                return null; // new TrainingPresenterImpl(activity, view);
            case QUIZ_MODE:
                return null; // new QuizPresenterImpl(activity, view );
            default:
                return null;
        }
    }
}