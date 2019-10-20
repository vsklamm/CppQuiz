package com.vsklamm.cppquiz.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.vsklamm.cppquiz.data.MainRepository;
import com.vsklamm.cppquiz.data.MainRepositoryImpl;
import com.vsklamm.cppquiz.data.model.PublishedDatabase;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.data.remore.APIService;
import com.vsklamm.cppquiz.utils.RequestType;
import com.vsklamm.cppquiz.utils.TimeWork;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class MainPresenter {

    private static final String CPP_STANDARD = "CPP_STANDARD";
    private static final String HAS_VISITED = "HAS_VISITED";

    private static volatile MainPresenter mainPresenterInstance;
    private WeakReference<MainPresenter.MainPresenterCallbacks> listener;
    private MainRepository mainRepository;

    private String cppStandard;
    private List<Question> existingQuestions;

    private MainPresenter() {
        if (mainPresenterInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static MainPresenter getInstance() {
        if (mainPresenterInstance == null) {
            synchronized (MainPresenter.class) {
                if (mainPresenterInstance == null) {
                    mainPresenterInstance = new MainPresenter();
                }
            }
        }
        return mainPresenterInstance;
    }

    void initGame(@NonNull Context context, @NonNull APIService apiService, @NonNull SharedPreferences appPreferences) {
        mainRepository = new MainRepositoryImpl();
        try {
            listener = new WeakReference<>((MainPresenter.MainPresenterCallbacks) context);
        } catch (ClassCastException e) {
            // ignore
        }
        boolean hasVisited = appPreferences.getBoolean(HAS_VISITED, false);
        if (!hasVisited) {
            listener.get().onLoadingDatabaseFirstTime();
            loadRemoteDatabase(apiService, RequestType.LOAD);
            return;
        }
        if (TimeWork.isNextDay(appPreferences)) {
            loadRemoteDatabase(apiService, RequestType.UPDATE);
        }
        cppStandard = appPreferences.getString(CPP_STANDARD, "C++17"); // for many years
        Single<List<Integer>> singleQuestionIds = mainRepository.getQuestionsIds();
        singleQuestionIds.subscribe(new ListIntDisposableSingleObserver());
    }

    void updateDatabase(@NonNull APIService apiService) {
        loadRemoteDatabase(apiService, RequestType.LOAD);
    }

    private void loadRemoteDatabase(APIService apiService, final RequestType requestType) {
        Single<List<Question>> singleQuestions = mainRepository.getQuestions();
        singleQuestions.subscribe(new ListQuestDisposableSingleObserver());
        Observable<PublishedDatabase> observablePublishedDatabase = mainRepository.getRemoteDatabase(apiService);
        observablePublishedDatabase.subscribe(new PublishedDatabaseObserver(requestType));
    }

    public interface MainPresenterCallbacks {

        void onLoadingDatabaseFirstTime();

        void onDatabaseUpdated(boolean changed, LinkedHashSet<Integer> questionIds);

        void onDatabaseReady(String cppStandard, LinkedHashSet<Integer> questionIds);

        void onLoadDatabaseError();

    }

    private class ListQuestDisposableSingleObserver extends DisposableSingleObserver<List<Question>> {
        @Override
        public void onSuccess(List<Question> questions) {
            existingQuestions = questions;
        }

        @Override
        public void onError(Throwable e) {
            listener.get().onLoadDatabaseError();
        }
    }

    private class ListIntDisposableSingleObserver extends DisposableSingleObserver<List<Integer>> {
        @Override
        public void onSuccess(List<Integer> questionIds) {
            LinkedHashSet<Integer> ids = new LinkedHashSet<>(questionIds); // TODO: maybe not
            listener.get().onDatabaseReady(cppStandard, ids);
        }

        @Override
        public void onError(Throwable e) {
            listener.get().onLoadDatabaseError();
        }
    }

    private class PublishedDatabaseObserver implements Observer<PublishedDatabase> {
        private final RequestType requestType;

        PublishedDatabaseObserver(RequestType requestType) {
            this.requestType = requestType;
        }

        @Override
        public void onSubscribe(Disposable d) {
            // ignore?
        }

        @Override
        public void onNext(PublishedDatabase publishedDatabase) {
            LinkedHashSet<Integer> ids = new LinkedHashSet<>();
            for (Question q : publishedDatabase.getQuestions()) {
                ids.add(q.getId());
            }
            if (requestType == RequestType.LOAD) {
                mainRepository.saveDatabase(publishedDatabase.getQuestions());
                listener.get().onDatabaseReady(publishedDatabase.getCppStandard(), ids);
            } else {
                boolean updated = false;
                if (publishedDatabase.getQuestions().size() == mainRepository.getDatabaseSize()) {
                    for (Question q : publishedDatabase.getQuestions()) {
                        if (!existingQuestions.contains(q)) {
                            updated = true;
                            break;
                        }
                    }
                } else {
                    updated = true;
                }
                mainRepository.saveDatabase(publishedDatabase.getQuestions());
                listener.get().onDatabaseUpdated(updated, ids);
            }
        }

        @Override
        public void onError(Throwable e) {
            listener.get().onLoadDatabaseError();
        }

        @Override
        public void onComplete() {
            // ignore?
        }
    }

    //        private boolean isOnline() {
//            ConnectivityManager cm = (ConnectivityManager)
//                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
//            return netInfo != null && netInfo.isConnectedOrConnecting();
//        }
}
