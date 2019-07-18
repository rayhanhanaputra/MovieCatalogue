package com.example.moviecatalogue.network;

import com.example.moviecatalogue.entity.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("results")
    Call<Movie> getReleaseNow(@Query("api_key") String apiKey);
}
