package com.vsklamm.cppquiz.di.module;

import com.google.gson.Gson;
import com.vsklamm.cppquiz.BuildConfig;
import com.vsklamm.cppquiz.data.remote.APIService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

public class NetworkModule extends Module {

    private final Gson gson = provideGson();
    private final Retrofit retrofit = provideRetrofit();

    public NetworkModule() {
        bind(Gson.class).toInstance(gson);
        bind(Retrofit.class).toInstance(retrofit);
        bind(APIService.class).toInstance(provideAPIService());
    }

    Gson provideGson() {
        return new Gson();
    }

    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIService provideAPIService() {
        return retrofit.create(APIService.class);
    }

}
