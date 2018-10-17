package com.vsklamm.cppquiz.utils;

import com.squareup.moshi.JsonReader;
import com.vsklamm.cppquiz.data.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okio.Okio;

public class Parser {

    public static DumpDataType<List<Question>> readJsonStream(InputStream in) throws IOException {

        try (JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(in)))) {
            return readQuestionList(reader);
        }
    }

    private static DumpDataType<List<Question>> readQuestionList(JsonReader reader) throws IOException {
        int dumpVersion = 1;
        String cppStandard = "";
        List<Question> questions = new ArrayList<>();

        reader.beginObject();

        if (reader.nextName().equals("version")) {
            dumpVersion = reader.nextInt();
        }
        if (reader.nextName().equals("cpp_standard")) {
            cppStandard = reader.nextString();
        }

        if (reader.nextName().equals("questions")) {
            reader.beginArray();
            while (reader.hasNext()) {
                questions.add(readQuestion(reader));
            }
            reader.endArray();
        }
        reader.endObject();
        return new DumpDataType<>(dumpVersion, cppStandard, questions);
    }

    private static Question readQuestion(JsonReader reader) throws IOException {
        int id = 0;
        int difficulty = 0;
        ResultBehaviourType result = ResultBehaviourType.OK;
        String answer = "", code = "", hint = "", explanation = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "hint":
                    hint = reader.nextString();
                    break;
                case "explanation":
                    explanation = reader.nextString();
                    break;
                case "question":
                    code = reader.nextString();
                    break;
                case "difficulty":
                    difficulty = reader.nextInt();
                    break;
                case "result":
                    result = ResultBehaviourType.getBehaviourType(reader.nextString());
                    break;
                case "answer":
                    answer = reader.nextString();
                    break;
                case "id":
                    id = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();
        return new Question(id, difficulty, result, answer, code, hint, explanation);
    }

}
