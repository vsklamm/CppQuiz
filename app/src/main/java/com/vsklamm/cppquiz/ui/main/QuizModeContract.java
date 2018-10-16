package com.vsklamm.cppquiz.ui.main;

import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.ui.BaseModeContract;
import com.vsklamm.cppquiz.ui.BasePresenter;
import com.vsklamm.cppquiz.ui.BaseView;

public interface QuizModeContract extends BaseModeContract {

    interface View extends BaseView<Presenter> {

        void onGameStateChanged(final int questionId, final int correct, final int all); // diff

        void onQuestionLoaded(@NonNull final Question question, int attemptsRequired); // diff or with extra method (progress)

        void onHintReceived(@NonNull final String hint); // diff or with extra method (score)

        void onCorrectAnswered(@NonNull final Question question, final int attemptsRequired); // diff or with extra method (progress)

        void onIncorrectAnswered(final int attemptsRequired); // diff or with extra method (progress)

        void onFinishQuiz();

    }

    interface Presenter extends BasePresenter {

    }

}
