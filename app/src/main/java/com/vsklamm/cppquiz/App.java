package com.vsklamm.cppquiz;

import android.app.Application;

import androidx.room.Room;

import com.vsklamm.cppquiz.data.UserData;
import com.vsklamm.cppquiz.data.database.AppDatabase;
import com.vsklamm.cppquiz.ui.main.GameLogic;

public class App extends Application {

    private static App instance;

    private AppDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "questions")
                .build();
        UserData.getInstance();
        GameLogic.getInstance();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
