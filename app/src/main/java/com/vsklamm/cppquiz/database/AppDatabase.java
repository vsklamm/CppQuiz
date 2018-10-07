package com.vsklamm.cppquiz.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.vsklamm.cppquiz.model.Question;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
}
