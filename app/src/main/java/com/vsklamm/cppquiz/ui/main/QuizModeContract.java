package com.vsklamm.cppquiz.ui.main;

import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.ui.BaseModeContract;
import com.vsklamm.cppquiz.ui.BasePresenter;
import com.vsklamm.cppquiz.ui.BaseView;

public interface QuizModeContract {

    interface View extends BaseModeContract.View {

        void onQuizStateChanged(final int passedQuestions, final int totalQuestions, final float points);

        void onQuestionLoaded(@NonNull final Question question);

        void onQuizHintReceived(@NonNull final String hint);

        void onQuizCorrectAnswered(@NonNull final Question question);

        void onQuizIncorrectAnswered();

        void onFinishQuiz();

    }

    interface Presenter extends BaseModeContract.Presenter {

    }

}
