package com.vsklamm.cppquiz.utils;

import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class TimeWork {

    public static final String LAST_UPDATE = "LAST_UPDATE";

    public static final long LOADING_VIEW_DELAY = 200;

    public static boolean isNextDay(SharedPreferences appPreferences) { // TODO: is it legal
        DateTime lastUpdate = new DateTime(appPreferences.getLong(LAST_UPDATE, 0));
        DateTime currentDate = new DateTime()
                .withTimeAtStartOfDay()
                .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("CET")));
        return lastUpdate.isBefore(currentDate);
    }

}
