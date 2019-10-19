package com.vsklamm.cppquiz.data.api;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class CppQuizLiteApi {

    private static final HttpUrl URL = HttpUrl.parse("http://cppquiz.org");

    private CppQuizLiteApi() {
    }

    /**
     * Returns {@link Request} for executing request to load dump
     */
    public static Request getDumpRequest() {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(URL.scheme())
                .host(URL.host())
                .addPathSegments("static\\published.json")
                .build();

        return new Request.Builder()
                .url(url)
                .build();
    }

    public static String getQuestionURL(final int questionId) {
        return new HttpUrl.Builder()
                .scheme(URL.scheme())
                .host(URL.host())
                .addPathSegments("quiz\\question")
                .addPathSegment(Integer.toString(questionId))
                .build()
                .toString();
    }
}
