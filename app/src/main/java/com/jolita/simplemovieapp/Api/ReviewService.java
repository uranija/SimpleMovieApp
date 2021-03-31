package com.jolita.simplemovieapp.Api;

import com.jolita.simplemovieapp.model.ReviewResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {
    //Reviews
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResult> getReview(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
