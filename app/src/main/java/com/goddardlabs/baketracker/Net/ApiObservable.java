package com.goddardlabs.baketracker.Net;

import com.goddardlabs.baketracker.Parcelables.Recipe;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiObservable {
    @GET(" ")
    Observable<ArrayList<Recipe>> getRecipes();
}
