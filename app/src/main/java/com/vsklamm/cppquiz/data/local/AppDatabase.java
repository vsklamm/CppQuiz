package com.vsklamm.cppquiz.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vsklamm.cppquiz.data.model.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
}
