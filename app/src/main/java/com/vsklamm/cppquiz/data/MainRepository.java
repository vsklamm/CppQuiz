package com.vsklamm.cppquiz.data;

import com.vsklamm.cppquiz.data.model.PublishedDatabase;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.data.remore.APIService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface MainRepository {

    Observable<PublishedDatabase> getRemoteDatabase(APIService apiService);

    Single<List<Question>> getQuestions();

    Single<List<Integer>> getQuestionsIds();

    void saveDatabase(List<Question> questions);

    Integer getDatabaseSize();

}
