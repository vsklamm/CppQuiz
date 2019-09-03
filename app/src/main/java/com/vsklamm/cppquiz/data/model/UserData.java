package com.vsklamm.cppquiz.data.model;

import android.content.SharedPreferences;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.prefs.SharedPreferencesHelper;
import com.vsklamm.cppquiz.ui.main.GameLogic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;

import static android.content.Context.MODE_PRIVATE;

public class UserData implements Serializable {

    private static final String USER_QUIZ_DATA = "USER_QUIZ_DATA";
    private static final String FAVOURITE_QUESTIONS = "FAVOURITE_QUESTIONS";
    private static final String CORRECTLY_ANSWERED = "CORRECTLY_ANSWERED", ATTEMPTS = "ATTEMPTS";
    private static volatile UserData sSoleInstance;

    public UsersAnswer givenAnswer;

    private SharedPreferences userQuizData;

    private LinkedHashSet<Integer> correctlyAnswered;
    private HashMap<Integer, Integer> attempts;
    private LinkedHashSet<Integer> favouriteQuestions;

    private UserData() {
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        userQuizData = App.getInstance().getSharedPreferences(USER_QUIZ_DATA, MODE_PRIVATE);
        favouriteQuestions = SharedPreferencesHelper.getFromJson(userQuizData, FAVOURITE_QUESTIONS);
        correctlyAnswered = SharedPreferencesHelper.getFromJson(userQuizData, CORRECTLY_ANSWERED);
        attempts = SharedPreferencesHelper.getHashMap(userQuizData, ATTEMPTS);
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

    public boolean isFavouriteQuestion(final int questionId) {
        return favouriteQuestions.contains(questionId);
    }

    public LinkedHashSet<Integer> getCorrectlyAnsweredQuestions() {
        return correctlyAnswered;
    }

    public HashMap<Integer, Integer> getAttempts() {
        return attempts;
    }

    public LinkedHashSet<Integer> getFavouriteQuestions() {
        return favouriteQuestions;
    }

    public void registerCorrectAnswer(final int questionId) {
        correctlyAnswered.add(questionId);
    }

    public void registerAttempt(final int questionId) {
        if (attempts.get(questionId) == null)
            attempts.put(questionId, 0);
        Integer old = attempts.get(questionId);
        attempts.put(questionId, old + 1);
    }

    public void clearCorrectAnswers() {
        attempts.clear();
        correctlyAnswered.clear();
        saveAttempts();
        saveCorrectlyAnswered();
        GameLogic.getInstance().ourQuestion();
    }

    public void addToFavouriteQuestions(final int questionId) {
        favouriteQuestions.add(questionId);
        saveFavouriteQuestions();
    }

    public void deleteFromFavouriteQuestions(final int questionId) {
        favouriteQuestions.remove(questionId);
        saveFavouriteQuestions();
    }

    public int attemptsGivenFor(final int questionId) {
        if (attempts.get(questionId) == null)
            attempts.put(questionId, 0);
        return attempts.get(questionId);
    }

    public void saveAttempts() {
        SharedPreferencesHelper.saveCollection(userQuizData, ATTEMPTS, attempts);
    }

    public void saveFavouriteQuestions() {
        SharedPreferencesHelper.saveCollection(userQuizData, FAVOURITE_QUESTIONS, favouriteQuestions);
    }

    public void saveCorrectlyAnswered() {
        SharedPreferencesHelper.saveCollection(userQuizData, CORRECTLY_ANSWERED, correctlyAnswered);
    }

}
