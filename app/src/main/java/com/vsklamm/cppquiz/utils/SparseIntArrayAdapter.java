package com.vsklamm.cppquiz.utils;

import com.squareup.moshi.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

import okio.Okio;

public class SparseIntArrayAdapter {

    public static HashMap<Integer, Integer> readJsonStream(String in) throws IOException {
        try (JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))))) {
            return readHashMap(reader);
        }
    }

    private static HashMap<Integer, Integer> readHashMap(JsonReader jsonReader) throws IOException {
        int size = 0;
        LinkedList<Integer> keys = new LinkedList<>();
        LinkedList<Integer> values = new LinkedList<>();

        jsonReader.beginObject();
        if (jsonReader.nextName().equals("mKeys")) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                keys.add(jsonReader.nextInt());
            }
            jsonReader.endArray();
        }
        if (jsonReader.nextName().equals("mSize")) {
            size = jsonReader.nextInt();
        }
        if (jsonReader.nextName().equals("mValues")) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                values.add(jsonReader.nextInt());
            }
            jsonReader.endArray();
        }
        jsonReader.endObject();
        HashMap<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            result.put(keys.get(i), values.get(i));
        }
        return result;
    }
}