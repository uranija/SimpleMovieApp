package com.jolita.simplemovieapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;
import com.jolita.simplemovieapp.Api.Client;
import com.jolita.simplemovieapp.Api.ReviewService;
import com.jolita.simplemovieapp.Api.VideoService;
import com.jolita.simplemovieapp.adapter.ReviewAdapter;
import com.jolita.simplemovieapp.adapter.VideoAdapter;
import com.jolita.simplemovieapp.database.AppDatabase;
import com.jolita.simplemovieapp.database.FavoriteEntry;
import com.jolita.simplemovieapp.model.Movie;
import com.jolita.simplemovieapp.model.Review;
import com.jolita.simplemovieapp.model.ReviewResult;
import com.jolita.simplemovieapp.model.Video;
import com.jolita.simplemovieapp.model.VideoResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jolita.simplemovieapp.BuildConfig.API_KEY;

public class DetailActivity extends AppCompatActivity {
    TextView movieName, plotOverview, userRating, releaseDate;
    ImageView imageView;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private RecyclerView recyclerView2;
    private ReviewAdapter reviewAdapter;
    private List<Video> videoList;
    private List<Review> reviewList;
    Movie movie;
    String moviePoster, movieTitle, overview, rating, dateOfRelease;
    int movie_id;
    // Member variable for the Database
    private AppDatabase mDb;

    List<FavoriteEntry> entries = new ArrayList<>();
    private final AppCompatActivity activity = DetailActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());

        imageView = (ImageView) findViewById(R.id.thumbnail_image);
        movieName = (TextView) findViewById(R.id.title);
        plotOverview = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);

        /*
         * Here is where all the magic happens. The getIntent method will give us the Intent that
         * started this particular Activity.
         */

        Intent intentThatStartedThisActivity = getIntent();

        /*
         * Although there is always an Intent that starts any particular Activity, we can't
         * guarantee that the extra we are looking for was passed as well. Because of that, we need
         * to check to see if the Intent has the extra that we specified when we created the
         * Intent that we use to start this Activity. Note that this extra may not be present in
         * the Intent if this Activity was started by any other method.
         * */

        if (intentThatStartedThisActivity.hasExtra("Movie")) {

            /*
             * Now that we've checked to make sure the extra we are looking for is contained within
             * the Intent, we can extract the extra. To do that, we simply call the getStringExtra
             * method on the Intent. There are various other get*Extra methods you can call for
             * different types of data. Please feel free to explore those yourself.
             */
            movie = getIntent().getParcelableExtra("Movie");

            moviePoster = movie.getPosterPath();
            movieTitle = movie.getOriginalTitle();
            overview = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();

            //String moviePoster = getIntent().getExtras().getString("poster_path");
            // String title = getIntent().getExtras().getString("original_title");
            // String overview = getIntent().getExtras().getString("overview");
            //String voteAverage = getIntent().getExtras().getString("vote_average");
            //String ReleaseDate = getIntent().getExtras().getString("release_date");

            /*
             * Finally, we can set the text of our TextView (using setText) to the text that was
             * passed to this Activity.
             *
             */
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + moviePoster)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.load)
                    .into(imageView);


            movieName.setText(movieTitle);
            plotOverview.setText(overview);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        initVideos();
        checkStatus(movieTitle);
        initReviews();

    }

    private void initVideos() {
        videoList = new ArrayList<>();
        videoAdapter = new VideoAdapter(this, videoList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();

        loadMovieVideos();
        //loadReviews();
    }

    private void initReviews() {
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewList);

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.setAdapter(reviewAdapter);
        videoAdapter.notifyDataSetChanged();


        loadReviews();
    }


    private void loadMovieVideos() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Get your API Key ", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            VideoService apiService = Client.getClient().create(VideoService.class);
            Call<VideoResult> call = apiService.getMovieVideos(movie_id, API_KEY);
            call.enqueue(new Callback<VideoResult>() {
                @Override
                public void onResponse(Call<VideoResult> call, Response<VideoResult> response) {
                    List<Video> videos = response.body().getResults();
                    recyclerView.setAdapter(new VideoAdapter(getApplicationContext(), videos));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<VideoResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadReviews() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Get your API Key", Toast.LENGTH_SHORT).show();
                return;
            }
            Client Client = new Client();
            ReviewService apiService = Client.getClient().create(ReviewService.class);
            Call<ReviewResult> call = apiService.getReview(movie_id, API_KEY);
            call.enqueue(new Callback<ReviewResult>() {
                @Override
                public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                    List<Review> reviews = response.body().getResults();
                    recyclerView2.setAdapter(new ReviewAdapter(getApplicationContext(), reviews));
                    recyclerView2.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<ReviewResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Was using this tutorial to implement favorite movie option with room database :
     * "https://github.com/delaroy/MoviesApp/blob/room/app/src/main/java/com/delaroystudios/movieapp/DetailActivity.java"
     **/
    public void saveFavoriteMovie() {
        Double rate = movie.getVoteAverage();
        final FavoriteEntry favoriteEntry = new FavoriteEntry(movie_id, moviePoster, movieTitle, rate, dateOfRelease, overview);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertFavorite(favoriteEntry);
            }
        });
    }

    private void deleteFavoriteMovie(final int movie_id) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().deleteFavoriteWithId(movie_id);
            }
        });
    }

    // Using material favorite button to favorite movies from https://github.com/IvBaranov/MaterialFavoriteButton //
    @SuppressLint("StaticFieldLeak")
    private void checkStatus(final String movieName) {
        final MaterialFavoriteButton materialFavoriteButton = (MaterialFavoriteButton) findViewById(R.id.favorite_button);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                entries.clear();
                entries = mDb.favoriteDao().loadAll(movieName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (entries.size() > 0) {
                    materialFavoriteButton.setFavorite(true);
                    materialFavoriteButton.setOnFavoriteChangeListener(
                            new MaterialFavoriteButton.OnFavoriteChangeListener() {
                                @Override
                                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                    if (favorite == true) {
                                        saveFavoriteMovie();
                                        Snackbar.make(buttonView, "Added to Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        deleteFavoriteMovie(movie_id);
                                        Snackbar.make(buttonView, "Removed from Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    materialFavoriteButton.setOnFavoriteChangeListener(
                            new MaterialFavoriteButton.OnFavoriteChangeListener() {
                                @Override
                                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                                    if (favorite == true) {
                                        saveFavoriteMovie();
                                        Snackbar.make(buttonView, "Added to Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        int movie_id = getIntent().getExtras().getInt("id");
                                        deleteFavoriteMovie(movie_id);
                                        Snackbar.make(buttonView, "Removed from Favorite",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        }.execute();
    }


}
