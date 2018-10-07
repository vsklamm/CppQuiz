package com.vsklamm.cppquiz.utils;

public enum RequestType {

    LOAD_DUMP(0),

    UPDATE(1);

    private final int numeral;

    RequestType(final int num) {
        this.numeral = num;
    }

    public static RequestType getRequestType(final int numeral) {
        for (RequestType ds : values()) {
            if (ds.numeral == numeral) {
                return ds;
            }
        }
        return LOAD_DUMP;
    }
}
