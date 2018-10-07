package com.vsklamm.cppquiz.api;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CppQuizLiteApi {

    private static final Uri BASE_URI = Uri.parse("http://cppquiz.org");

    private CppQuizLiteApi() {
    }

    /**
     * Returns {@link HttpURLConnection} for executing request to load dump
     */
    public static HttpURLConnection getDumpRequest() throws IOException {
        String request = BASE_URI.buildUpon()
                .appendPath("static")
                .appendPath("published.json")
                .toString();

        return (HttpURLConnection) new URL(request).openConnection();
    }

    /**
     * Returns {@link HttpURLConnection} for executing request to load quiz by key
     */
    public static HttpURLConnection getQuizRequest(final String quizKey) throws IOException {
        String request = BASE_URI.buildUpon()
                .appendPath("api")
                .appendPath("v1")
                .appendPath("quiz")
                .appendPath("quiz")
                .appendQueryParameter("key", quizKey)
                .toString();

        return (HttpURLConnection) new URL(request).openConnection();
    }


    public static String getQuestionURL(final int questionId) {
        return BASE_URI.buildUpon()
                .appendPath("quiz")
                .appendPath("question")
                .appendPath(Integer.toString(questionId))
                .toString();
    }

    public static boolean isCorrectQuizPath(final String quizKey) {
        return false;
    }
}
