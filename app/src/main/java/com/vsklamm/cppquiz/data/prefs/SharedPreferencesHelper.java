package com.vsklamm.cppquiz.data.prefs;

import android.content.SharedPreferences;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {

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

    public static <T> void saveCollection(SharedPreferences prefs, String key, T collection) {
        SharedPreferences.Editor editor = prefs.edit();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(collection.getClass());
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);
        String json = jsonAdapter.toJson(collection);
        editor.putString(key, json);
        editor.apply();
    }

    public static LinkedHashSet<Integer> getFromJson(SharedPreferences prefs, String key)
    {
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new LinkedHashSet<>();
        }
        Type type = Types.newParameterizedType(Set.class, Integer.class);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Set<Integer>> adapter = moshi.adapter(type);
        try {
            return (LinkedHashSet<Integer>) adapter.fromJson(json);
        } catch (IOException e) {
            return new LinkedHashSet<>();
        }
    }

    public static HashMap<Integer, Integer> getSparseInt(SharedPreferences prefs, String key) { // TODO: erase copy-paste #4
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new HashMap<>();
        }
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(Map.class, Integer.class, Integer.class);
        JsonAdapter<Map<Integer, Integer>> adapter = moshi.adapter(type);
        try {
            return (HashMap<Integer, Integer>) adapter.fromJson(json);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
