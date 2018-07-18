package com.goddardlabs.baketracker.Net;

import com.goddardlabs.baketracker.Parcelables.Recipe;

import java.util.ArrayList;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class NetworkService {
    private ApiObservable mApiObservable;
    private static Retrofit retrofitBuilder = null;

    public NetworkService() {
        mApiObservable = this.getJsonRecipes().create(ApiObservable.class);
    }

    public Retrofit getJsonRecipes() {
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder()
                    .baseUrl("http://go.udacity.com/android-baking-app-json/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofitBuilder;
    }

    public Observable<ArrayList<Recipe>> networkApiRequestRecipes() {
        Observable observer = mApiObservable.getRecipes();
        return observer;
    }
}
