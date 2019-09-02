package com.vsklamm.cppquiz.data;

import java.util.HashMap;
import java.util.LinkedHashSet;

public interface Repository {

    void questionById(final int questionId);


    /*
        FROM USER DATA
    */
    LinkedHashSet<Integer> getCorrectlyAnsweredQuestions();

    HashMap<Integer, Integer> getAttempts();

    LinkedHashSet<Integer> getFavouriteQuestions();

}