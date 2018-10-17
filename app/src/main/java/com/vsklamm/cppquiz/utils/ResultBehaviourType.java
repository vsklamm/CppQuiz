package com.vsklamm.cppquiz.utils;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

/**
 * What the c++ program will do
 */
public enum ResultBehaviourType {

    /**
     * The program is guaranteed to output something
     */
    OK("OK"),

    /**
     * The program has a compilation error
     */
    CE("CE"),

    /**
     * The program is unspecified / implementation defined
     */
    US("US"),

    /**
     * The program is undefined
     */
    UD("UD");

    private final String stringName;

    ResultBehaviourType(final String stringName) {
        this.stringName = stringName;
    }

    @TypeConverter
    @NonNull
    public static ResultBehaviourType getBehaviourType(final String stringName) {
        return ResultBehaviourType.valueOf(stringName);
    }

    @TypeConverter
    @NonNull
    public static String toString(final ResultBehaviourType status) {
        return status.stringName;
    }

    public static ResultBehaviourType getType(final int numeral) {
        for (ResultBehaviourType res : values()) {
            if (res.ordinal() == numeral) {
                return res;
            }
        }
        return OK;
    }

}
