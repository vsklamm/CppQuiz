package com.vsklamm.cppquiz.ui.main.quiz;

import android.support.annotation.NonNull;

import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.ui.BaseView;

public interface QuizView extends BaseView {

    void onQuizStateChanged(final int passedQuestions, final int totalQuestions, final float points);

    void onQuestionLoaded(@NonNull final Question question);

    void onQuizHintReceived(@NonNull final String hint);

    void onQuizCorrectAnswered(@NonNull final Question question);

    void onQuizIncorrectAnswered();

    void onFinishQuiz();

}
