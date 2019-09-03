package com.vsklamm.cppquiz.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.database.AppDatabase;

import toothpick.config.Module;

public class AppModule extends Module {

    public static final String APP_PREFERENCES = "APP_PREFERENCES", APP_PREF_ZOOM = "APP_PREF_ZOOM",
            APP_PREF_LINE_NUMBERS = "APP_PREF_LINE_NUMBERS", THEME = "THEME";

    private final App app;

    public AppModule(App app) {
        this.app = app;
        bind(App.class).toInstance(app);
    }

    SharedPreferences providePreferences() {
        return app.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    // not database but storage
    AppDatabase provideAppDAtabase() {
        return Room.databaseBuilder(app.getApplicationContext(), AppDatabase.class, "questions").build();
    }

}
