package com.vsklamm.cppquiz.ui.main;

import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.ui.BaseModeContract;
import com.vsklamm.cppquiz.ui.BasePresenter;
import com.vsklamm.cppquiz.ui.BaseView;

public interface TrainingModeContract extends BaseModeContract {

    interface View extends BaseView<Presenter> {

        void onGameStateChanged(final int questionId, final int correct, final int all);

        void onQuestionLoaded(@NonNull final Question question, int attemptsRequired);

        void onHintReceived(@NonNull final String hint);

        void onCorrectAnswered(@NonNull final Question question, final int attemptsRequired);

        void onIncorrectAnswered(final int attemptsRequired);

        void onGiveUp(@NonNull final Question question);

        void tooEarlyToGiveUp(final int attemptsRequired);

        void noMoreQuestions();

    }

    interface Presenter extends BasePresenter {

    }

}