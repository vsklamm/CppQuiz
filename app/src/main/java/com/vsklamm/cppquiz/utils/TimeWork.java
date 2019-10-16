package com.vsklamm.cppquiz.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class TimeWork {

    public static final long LOADING_VIEW_DELAY = 200;

    public static boolean isNextDay(final Long lastUpdateTime) {
        final DateTime lastUpdate = new DateTime(lastUpdateTime);
        final DateTime currentDate = new DateTime()
                .withTimeAtStartOfDay()
                .withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("CET")));
        return lastUpdate.isBefore(currentDate);
    }

}
