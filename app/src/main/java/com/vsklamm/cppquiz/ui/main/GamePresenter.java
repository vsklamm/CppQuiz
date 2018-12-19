package com.vsklamm.cppquiz.ui.main;

import com.vsklamm.cppquiz.data.Question;
import com.vsklamm.cppquiz.ui.BasePresenter;

public interface GamePresenter extends BasePresenter {

    void nextQuestion();

    void getHint();

    boolean checkAnswer(); // check if hint used for quizMode

    void giveUp();

    Question getCurrentQuestion();

}