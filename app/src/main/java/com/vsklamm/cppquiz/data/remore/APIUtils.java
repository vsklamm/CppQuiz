package com.vsklamm.cppquiz.data.remore;

public class APIUtils {

    private APIUtils() {
    }

    public static final String BASE_URL = "http://cppquiz.org/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
