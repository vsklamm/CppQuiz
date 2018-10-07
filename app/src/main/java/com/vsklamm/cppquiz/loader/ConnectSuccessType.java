package com.vsklamm.cppquiz.loader;

/**
 * Three possible results of the data loading process
 */
public enum ConnectSuccessType {

    /**
     * Data has been successfully loaded (online or offline)
     */
    OK(0),

    /**
     * Data not loaded due to lack of Internet
     */
    NO_INTERNET(1),

    /**
     * Data is not loaded for another reason
     */
    ERROR(2);

    private final int numeral;

    ConnectSuccessType(final int num) {
        this.numeral = num;
    }

    public static ConnectSuccessType getConnectType(final int numeral) {
        for (ConnectSuccessType ds : values()) {
            if (ds.numeral == numeral) {
                return ds;
            }
        }
        return ERROR;
    }

    public static Integer getInt(final ConnectSuccessType request) {
        return request.numeral;
    }
}