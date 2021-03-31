package com.jolita.simplemovieapp.Api;

import com.jolita.simplemovieapp.model.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/popular")
    Call<MovieResult> getPopularMovies(@Query("api_key") String apiKey);


    @GET("movie/top_rated")
    Call<MovieResult> getTopRatedMovies(@Query("api_key") String apiKey);

}
