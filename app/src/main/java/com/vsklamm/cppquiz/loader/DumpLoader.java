package com.vsklamm.cppquiz.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.MainActivity;
import com.vsklamm.cppquiz.api.CppQuizLiteApi;
import com.vsklamm.cppquiz.database.AppDatabase;
import com.vsklamm.cppquiz.database.QuestionDao;
import com.vsklamm.cppquiz.model.DumpDataType;
import com.vsklamm.cppquiz.model.Question;
import com.vsklamm.cppquiz.utils.Parser;
import com.vsklamm.cppquiz.utils.RequestType;
import com.vsklamm.cppquiz.utils.TimeWork;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DumpLoader extends AsyncTaskLoader<LoadResult<String, LinkedHashSet<Integer>>> {

    private final RequestType requestType;

    private WeakReference<MainActivity> callingActivity;
    private LoadResult<String, LinkedHashSet<Integer>> result;

    public DumpLoader(@NonNull MainActivity activity, final int requestType) {
        super(activity.getBaseContext());
        this.callingActivity = new WeakReference<>(activity);
        this.requestType = RequestType.getRequestType(requestType);
    }

    protected void onStartLoading() {
        if (result == null) {
            forceLoad();
        } else {
            deliverResult(result);
        }
    }

    @Nullable
    @Override
    public LoadResult<String, LinkedHashSet<Integer>> loadInBackground() {
        if (!isOnline()) {
            return new LoadResult<>(ConnectSuccessType.NO_INTERNET, null, null, requestType, false);
        }
        HttpURLConnection connection = null;
        try {
            LinkedHashSet<Integer> result = new LinkedHashSet<>();
            DumpDataType<List<Question>> newDump;

            updateProgress(System.currentTimeMillis(), "Loading questions");
            long startTime = System.currentTimeMillis();

            connection = CppQuizLiteApi.getDumpRequest();
            newDump = Parser.readJsonStream(connection.getInputStream());

            updateProgress(System.currentTimeMillis() - startTime, "Database processing");

            AppDatabase db = App.getInstance().getDatabase();
            QuestionDao questionDao = db.questionDao();

            boolean updated = false;
            switch (requestType) {
                case UPDATE:
                    if (newDump.questions.size() == questionDao.getSize()) {
                        List<Question> existingQuestions = new ArrayList<>(questionDao.getAll()); // TODO: -> O(n) || O(nlogn)
                        for (Question q : newDump.questions) {
                            if (!existingQuestions.contains(q)) {
                                updated = true;
                                break;
                            }
                        }
                        if (!updated) {
                            break;
                        }
                    }
                case LOAD_DUMP:
                    if (questionDao.getSize() != 0) {
                        questionDao.clearTable();
                    }
                    questionDao.insert(newDump.questions);
                    for (Question q : newDump.questions) {
                        result.add(q.getId());
                    }
                    updated = true;
                    break;
            }

            this.result = new LoadResult<>(ConnectSuccessType.OK, newDump.cppStandard, result, requestType, updated);
            connection.disconnect();
            return this.result;
        } catch (IOException | InterruptedException ex) {
            ConnectSuccessType connectSuccessType = isOnline() ? ConnectSuccessType.ERROR : ConnectSuccessType.NO_INTERNET;
            return new LoadResult<>(connectSuccessType, null, null, requestType, false);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void updateProgress(final long timePeriod, final String action) throws InterruptedException {
        if (timePeriod < TimeWork.LOADING_VIEW_DELAY) {
            Thread.sleep(TimeWork.LOADING_VIEW_DELAY - timePeriod);
        }
        if (callingActivity.get() != null) {
            callingActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callingActivity.get().progressTextViewLoading.setText(action);
                }
            });
        }
        Thread.sleep(TimeWork.LOADING_VIEW_DELAY);
    }

}
