package com.vsklamm.cppquiz.data.prefs;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {

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

    /*public static <T> void saveCollection(SharedPreferences prefs, String key, T collection) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(collection);
        Log.e(className, collection.getClass().getName() + " is recorded using gson:");
        Log.e(className, json);
        editor.putString(key, json);
        editor.apply();
    }*/

    public static void saveCollection(SharedPreferences prefs, String key, Set<Integer> collection) {
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

    public static void saveCollection(SharedPreferences prefs, String key, Map<Integer, Integer> collection) {
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

    public static LinkedHashSet<Integer> getFromJson(SharedPreferences prefs, String key) {
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
            return result == null ? new LinkedHashSet<Integer>() : result;
        } catch (IOException | NullPointerException ex) {
            return new LinkedHashSet<>();
        } catch (JsonDataException | ClassCastException ex) {
            Gson gson = new Gson();
            Log.e(className, "gson works");
            return gson.fromJson(json, type);
        }
    }

    public static HashMap<Integer, Integer> getHashMap(SharedPreferences prefs, String key) { // TODO: erase copy-paste #4
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
            return result == null ? new HashMap<Integer, Integer>() : result;
        } catch (IOException | NullPointerException ex) {
            return new HashMap<>();
        } catch (JsonDataException | ClassCastException ex) {
            Gson gson = new Gson();
            Log.e(className, "gson works");
            return gson.fromJson(json, type);
        }
    }
}