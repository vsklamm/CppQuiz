package com.vsklamm.cppquiz.ui.favourites;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.vsklamm.cppquiz.R;
import com.vsklamm.cppquiz.utils.ActivityUtils;

import static com.vsklamm.cppquiz.ui.main.MainActivity.APP_PREFERENCES;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        ActivityUtils.setUpTheme(this, appPreferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
    }
}