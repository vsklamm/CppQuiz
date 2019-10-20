package com.vsklamm.cppquiz.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.local.AppDatabase;
import com.vsklamm.cppquiz.data.local.QuestionDao;
import com.vsklamm.cppquiz.data.model.PublishedDatabase;
import com.vsklamm.cppquiz.data.model.Question;
import com.vsklamm.cppquiz.data.remore.APIService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainRepositoryImpl implements MainRepository {

    private QuestionDao questionDao;

    public MainRepositoryImpl() {
        AppDatabase db = App.getInstance().getDatabase();
        questionDao = db.questionDao();
    }

    public Observable<PublishedDatabase> getRemoteDatabase(APIService apiService) {
        return apiService.getPublishedQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Question>> getQuestions() {
        return questionDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Integer>> getQuestionsIds() {
        return questionDao.getAllIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void saveDatabase(List<Question> questions) {
        if (questionDao.getSize() != 0) {
            questionDao.clearTable();
        }
        questionDao.insert(questions); // TODO: Or it's bad?
    }

    public Integer getDatabaseSize() {
        return questionDao.getSize();
    }

    private final static String className = Log.class.getName();

    public static void save(SharedPreferences prefs, String key, Object value) {
        SharedPreferences.Editor editor = prefs.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.apply();
    }

    public static void saveUsersCorrectAnswers(SharedPreferences prefs, String key, Set<Integer> collection) {
        SharedPreferences.Editor editor = prefs.edit();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Set.class, Integer.class);
        JsonAdapter<Set<Integer>> jsonAdapter = moshi.adapter(type);
        String json = jsonAdapter.toJson(collection);
        Log.e(className, collection.getClass().getName() + " is recorded using Moshi:");
        Log.e(className, json);
        editor.putString(key, json);
        editor.apply();
    }

    public static void saveUsersAttempts(SharedPreferences prefs, String key, Map<Integer, Integer> collection) {
        SharedPreferences.Editor editor = prefs.edit();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, Integer.class, Integer.class);
        JsonAdapter<Map<Integer, Integer>> jsonAdapter = moshi.adapter(type);
        String json = jsonAdapter.toJson(collection);
        Log.e(className, collection.getClass().getName() + " is recorded using Moshi:");
        Log.e(className, json);
        editor.putString(key, json);
        editor.apply();
    }

    public static LinkedHashSet<Integer> getUsersCorrectAnswers(SharedPreferences prefs, String key) {
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new LinkedHashSet<>();
        }
        Type type = Types.newParameterizedType(Set.class, Integer.class);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Set<Integer>> adapter = moshi.adapter(type);
        try {
            Log.e(className, json);
            LinkedHashSet<Integer> result = new LinkedHashSet<>(adapter.fromJson(json));
            Log.e(className, "moshi works");
            return result == null ? new LinkedHashSet<>() : result;
        } catch (IOException | NullPointerException ex) {
            return new LinkedHashSet<>();
        } catch (JsonDataException | ClassCastException ex) {
            Gson gson = new Gson();
            Log.e(className, "gson works");
            return gson.fromJson(json, type);
        }
    }

    public static HashMap<Integer, Integer> getUsersAttempts(SharedPreferences prefs, String key) {
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new HashMap<>();
        }
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, Integer.class, Integer.class);
        JsonAdapter<Map<Integer, Integer>> adapter = moshi.adapter(type);
        try {
            Log.e(className, json);
            HashMap<Integer, Integer> result = new HashMap<>(adapter.fromJson(json));
            Log.e(className, "moshi works");
            return result == null ? new HashMap<>() : result;
        } catch (IOException | NullPointerException ex) {
            return new HashMap<>();
        } catch (JsonDataException | ClassCastException ex) {
            Gson gson = new Gson();
            Log.e(className, "gson works");
            return gson.fromJson(json, type);
        }
    }

}
