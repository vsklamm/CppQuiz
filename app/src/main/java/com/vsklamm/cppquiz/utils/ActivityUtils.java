package com.vsklamm.cppquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vsklamm.cppquiz.R;

public class ActivityUtils {

    private static final String APP_THEME_IS_DARK = "APP_THEME_IS_DARK";

    public static void setUpThemeNoActionBar(Context context, SharedPreferences preferences) {
        boolean isDark = preferences.getBoolean(APP_THEME_IS_DARK, false);
        context.setTheme(isDark ? R.style.AppTheme_Dark_NoActionBar : R.style.AppTheme_NoActionBar);
    }

    public static void setUpTheme(Context context, SharedPreferences preferences) {
        boolean isDark = preferences.getBoolean(APP_THEME_IS_DARK, false);
        context.setTheme(isDark ? R.style.AppTheme_Dark : R.style.AppTheme);
    }

}
