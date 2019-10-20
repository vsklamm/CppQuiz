package com.vsklamm.cppquiz;

import android.app.Application;

import androidx.room.Room;

import com.vsklamm.cppquiz.data.model.UserData;
import com.vsklamm.cppquiz.data.local.AppDatabase;
import com.vsklamm.cppquiz.ui.main.GameLogic;
import com.vsklamm.cppquiz.ui.main.MainPresenter;

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
        MainPresenter.getInstance();
        UserData.getInstance();
        GameLogic.getInstance();
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
