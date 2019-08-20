package com.example.organization.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Соединения с сервером.
public class NetworkClient {

    public  static final String BASE_URL = "http://46.20.207.198:9191";

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient() {

        if(retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
