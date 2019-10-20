package com.vsklamm.cppquiz.data.remore;

import com.vsklamm.cppquiz.data.model.PublishedDatabase;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("static/published.json/")
    Observable<PublishedDatabase> getPublishedQuestions();

    @GET("api/v1/quiz/quiz")
    Observable<String> getQuizByKey(@Query("key") String quizKey);

}