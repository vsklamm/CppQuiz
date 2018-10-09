package com.vsklamm.cppquiz.quizMVP;

import android.content.SharedPreferences;
import android.util.SparseIntArray;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.model.UsersAnswer;
import com.vsklamm.cppquiz.utils.SharedPreferencesUtils;

import java.io.Serializable;
import java.util.LinkedHashSet;

import static android.content.Context.MODE_PRIVATE;

public class UserData implements Serializable {

    private static final String USER_QUIZ_DATA = "USER_QUIZ_DATA";
    private static final String CORRECTLY_ANSWERED = "CORRECTLY_ANSWERED", ATTEMPTS = "ATTEMPTS";
    private static volatile UserData sSoleInstance;

    public UsersAnswer givenAnswer;

    private SharedPreferences userQuizData;

    private LinkedHashSet<Integer> correctlyAnswered;
    private SparseIntArray attempts;

    private UserData() {
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        userQuizData = App.getInstance().getSharedPreferences(USER_QUIZ_DATA, MODE_PRIVATE);

        correctlyAnswered = SharedPreferencesUtils.getFromGson(userQuizData, CORRECTLY_ANSWERED);
        attempts = SharedPreferencesUtils.getSparseInt(userQuizData, ATTEMPTS);
    }

    public static UserData getInstance() {
        if (sSoleInstance == null) {
            synchronized (UserData.class) {
                if (sSoleInstance == null) {
                    sSoleInstance = new UserData();
                }
            }
        }
        return sSoleInstance;
    }

    @SuppressWarnings("unused")
    protected UserData readResolve() {
        return getInstance();
    }

    public boolean isCorrectlyAnswered(final int questionId) {
        return correctlyAnswered.contains(questionId);
    }

    public LinkedHashSet<Integer> getCorrectlyAnsweredQuestions() {
        return correctlyAnswered;
    }

    public SparseIntArray getAttempts() {
        return attempts;
    }

    public void registerCorrectAnswer(final int questionId) {
        correctlyAnswered.add(questionId);
    }

    public void registerAttempt(final int questionId) {
        int old = attempts.get(questionId, 0);
        attempts.put(questionId, old + 1);
    }

    public void clearCorrectAnswers() {
        correctlyAnswered.clear();
        attempts.clear();
        saveUserData();
        GameLogic.getInstance().randomQuestion();
    }

    public int attemptsGivenFor(final int questionId) {
        return attempts.get(questionId, 0);
    }

    public void saveUserData() {
        SharedPreferencesUtils.saveCollection(userQuizData, ATTEMPTS, attempts);
        SharedPreferencesUtils.saveCollection(userQuizData, CORRECTLY_ANSWERED, correctlyAnswered);
    }

}
