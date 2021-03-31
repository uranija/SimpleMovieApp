package com.jolita.simplemovieapp.Api;

import com.jolita.simplemovieapp.model.VideoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VideoService {

    @GET("movie/{movie_id}/videos")
    Call<VideoResult> getMovieVideos(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
