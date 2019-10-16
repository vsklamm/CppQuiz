package com.vsklamm.cppquiz.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.vsklamm.cppquiz.utils.SparseIntArrayAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class UserRepositoryImpl implements UserRepository {

    private final static String className = Log.class.getName();
    private static final String HAS_VISITED = "HAS_VISITED";
    private static final String THEME = "THEME";
    private static final String LAST_UPDATE = "LAST_UPDATE";

    private final SharedPreferences sharedPreferences;


    @Inject
    public UserRepositoryImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public boolean isNewUser() {
        return sharedPreferences.getBoolean(HAS_VISITED, false);
    }

    @Override
    public String getCodeTheme() {
        return sharedPreferences.getString(THEME, "GITHUB");
    }

    @Override
    public Long getLastUpdateTime() {
        return sharedPreferences.getLong(LAST_UPDATE, 0);
    }

    @Override
    public void saveCollection(String key, Set<Integer> collection) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Moshi moshi = new Moshi.Builder().build();
        final Type type = Types.newParameterizedType(Set.class, Integer.class);
        JsonAdapter<Set<Integer>> jsonAdapter = moshi.adapter(type);
        final String json = jsonAdapter.toJson(collection);
        Log.e(className, collection.getClass().getName() + " is stored using Moshi:");
        editor.putString(key, json);
        editor.apply();
    }

    @Override
    public void saveCollection(String key, Map<Integer, Integer> collection) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Moshi moshi = new Moshi.Builder().build();
        final Type type = Types.newParameterizedType(Map.class, Integer.class, Integer.class);
        JsonAdapter<Map<Integer, Integer>> jsonAdapter = moshi.adapter(type);
        final String json = jsonAdapter.toJson(collection);
        Log.e(className, collection.getClass().getName() + " is recorded using Moshi:");
        Log.e(className, json);
        editor.putString(key, json).apply();
    }

    @Override
    public LinkedHashSet<Integer> getFromJson(String key) {
        final String json = sharedPreferences.getString(key, "");
        if (json.isEmpty()) {
            return new LinkedHashSet<>();
        }
        Type type = Types.newParameterizedType(Set.class, Integer.class);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Set<Integer>> adapter = moshi.adapter(type);
        try {
            Log.e(className, json);
            LinkedHashSet<Integer> result = new LinkedHashSet<>(adapter.fromJson(json));
            Log.e(className, "Moshi works");
            return result == null ? new LinkedHashSet<>() : result;
        } catch (IOException | NullPointerException | JsonDataException | ClassCastException ex) {
            return new LinkedHashSet<>();
        }
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public HashMap<Integer, Integer> getHashMap(String key) {
        String json = sharedPreferences.getString(key, "");
        if (json.isEmpty()) {
            return new HashMap<>();
        }
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, Integer.class, Integer.class);
        JsonAdapter<Map<Integer, Integer>> adapter = moshi.adapter(type);
        try {
            Log.e(className, json);
            HashMap<Integer, Integer> result = new HashMap<>(adapter.fromJson(json));
            Log.e(className, "Moshi works");
            return result == null ? new HashMap<Integer, Integer>() : result;
        } catch (IOException | NullPointerException ex) {
            return new HashMap<>();
        } catch (JsonDataException | ClassCastException ex) {
            Log.e(className, "hi, sparseIntArray");
            try {
                return SparseIntArrayAdapter.readJsonStream(json);
            } catch (IOException | NullPointerException except) {
                return new HashMap<>();
            }
        }
    }
}
