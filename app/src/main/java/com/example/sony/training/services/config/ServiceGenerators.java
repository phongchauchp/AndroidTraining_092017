package com.example.sony.training.services.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.sony.training.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phong on 11/17/17.
 */

public final class ServiceGenerators {

    private static final long TIMEOUT_CONNECTION = TimeUnit.MINUTES.toMillis(1);

    public static IGitHubApi createApiService(@NonNull Context context) {
        Retrofit retrofit = createRetrofit(context);

        return retrofit.create(IGitHubApi.class);
    }

    private static Retrofit createRetrofit(@NonNull Context context) {
        // Gson rules
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        // Initialize OkHttpClient
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        httpClientBuilder.cache(cache);
        httpClientBuilder.readTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        httpClientBuilder.connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);
        // show log when debug build
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            httpClientBuilder.addInterceptor(logging);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient okHttpClient = httpClientBuilder.build();

        return new Retrofit.Builder().baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }
}
