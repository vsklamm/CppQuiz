package com.vsklamm.cppquiz.di.module;

import android.content.SharedPreferences;

import androidx.room.Room;

import com.vsklamm.cppquiz.App;
import com.vsklamm.cppquiz.data.UserRepository;
import com.vsklamm.cppquiz.data.UserRepositoryImpl;
import com.vsklamm.cppquiz.data.database.AppDatabase;

import toothpick.config.Module;

import static android.content.Context.MODE_PRIVATE;

public class AppModule extends Module {

    public static final String
            APP_PREFERENCES = "APP_PREFERENCES",
            APP_PREF_ZOOM = "APP_PREF_ZOOM",
            APP_PREF_LINE_NUMBERS = "APP_PREF_LINE_NUMBERS", THEME = "THEME";

    private final App app;

    public AppModule(App app) {
        this.app = app;
        SharedPreferences sharedPreferences = app.getApplicationContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        bind(App.class).toInstance(app);
        bind(SharedPreferences.class).toInstance(sharedPreferences);
        bind(UserRepository.class).to(UserRepositoryImpl.class).singleton();
    }

    // not database but storage
    AppDatabase provideAppDAtabase() {
        return Room.databaseBuilder(app.getApplicationContext(), AppDatabase.class, "questions").build();
    }

}
