package com.vsklamm.cppquiz.data.prefs;

import android.content.SharedPreferences;
import android.util.SparseIntArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedHashSet;

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
        Gson gson = new Gson();
        String json = gson.toJson(collection);
        editor.putString(key, json);
        editor.apply();
    }

    public static LinkedHashSet<Integer> getFromGson(SharedPreferences prefs, String key) { // TODO: erase copy-paste #3
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new LinkedHashSet<>();
        }
        java.lang.reflect.Type type = new TypeToken<LinkedHashSet<Integer>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static SparseIntArray getSparseInt(SharedPreferences prefs, String key) { // TODO: erase copy-paste #4
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        if (json.equals("")) {
            return new SparseIntArray();
        }
        java.lang.reflect.Type type = new TypeToken<SparseIntArray>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
