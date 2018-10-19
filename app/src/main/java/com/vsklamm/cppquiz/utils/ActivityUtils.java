package com.vsklamm.cppquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vsklamm.cppquiz.R;

public class ActivityUtils {

    private static final String APP_THEME_IS_LIGHT = "APP_THEME_IS_LIGHT";

    public static void setUpTheme(Context context, SharedPreferences preferences) {
        boolean isLight = preferences.getBoolean(APP_THEME_IS_LIGHT, true);
        context.setTheme(isLight ? R.style.AppTheme_NoActionBar : R.style.DarkTheme);
    }

}
