package com.example.myapplication;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myRetro {
    private static Retrofit r = null;
    public static Retrofit getretrofit(Context c)
    {
        String url = c.getResources().getString(R.string.url);
        if (r == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            r = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return r;
    }
}
