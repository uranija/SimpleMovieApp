package com.jolita.simplemovieapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jolita.simplemovieapp.Api.Client;
import com.jolita.simplemovieapp.Api.MovieService;
import com.jolita.simplemovieapp.adapter.MovieAdapter;
import com.jolita.simplemovieapp.database.AppDatabase;
import com.jolita.simplemovieapp.database.FavoriteEntry;
import com.jolita.simplemovieapp.model.Movie;
import com.jolita.simplemovieapp.model.MovieResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    /**
     * API_KEY is left here for debugging, will delete it before uploading to my github repo
     **/
    //public static final String API_KEY = "Myapikey";
    String API_KEY = BuildConfig.API_KEY;

    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private MovieAdapter adapter;
    ProgressDialog pd;

    public static final String LOG_TAG = MovieAdapter.class.getName();
    // Member variable for the Database
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        startPopularMovies();
        startTopRated();


    }


    private void startPopularMovies() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        sortMovies();


    }

    /**
     * Method for getting information from the TMDB, will be using Retrofit library to fetch data.
     * Used this tutorial for getting TMDB data -
     * "https://www.supinfo.com/articles/single/7849-developing-popular-movies-application-in-android-using-retrofit"
     **/

    private void loadPopularMovies() {

        try {

            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid API key", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;

            }
            Client Client = new Client();
            MovieService apiMovieService =
                    Client.getClient().create(MovieService.class);
            Call<MovieResult> call = apiMovieService.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MovieResult>() {
                @Override
                public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);


                }

                @Override
                public void onFailure(Call<MovieResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }


    private void startTopRated() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //loadTopRatedMovies();
        getAllFavorite();

    }

    /**
     * Method for getting information from the TMDB, will be using Retrofit library to fetch data.
     * Used this tutorial for getting TMDB data -
     * "https://www.supinfo.com/articles/single/7849-developing-popular-movies-application-in-android-using-retrofit"
     **/
    private void loadTopRatedMovies() {

        try {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid API key", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;

            }
            Client Client = new Client();
            MovieService apiMovieService =
                    Client.getClient().create(MovieService.class);
            Call<MovieResult> call = apiMovieService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MovieResult>() {
                @Override
                public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);


                }

                @Override
                public void onFailure(Call<MovieResult> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }


    /**
     * Methods for setting up the menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.menu_movie, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "Preferences updated");
        sortMovies();
    }

    private void sortMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortMovies = preferences.getString(
                this.getString(R.string.pref_sort_movies_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortMovies.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "Sorting by most popular");
            loadPopularMovies();
        } else if (sortMovies.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
            startTopRated();
        } else {
            Log.d(LOG_TAG, "Sorting by vote average");
            loadTopRatedMovies();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (movieList.isEmpty()) {
            sortMovies();
        } else {

            sortMovies();
        }
    }


    /**
     * Was using this tutorial to implement favorite movie option with room database :
     * "https://github.com/delaroy/MoviesApp/blob/room/app/src/main/java/com/delaroystudios/movieapp/DetailActivity.java"
     **/
    private void getAllFavorite(){


        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorite().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> imageEntries) {
                List<Movie> movies = new ArrayList<>();
                for (FavoriteEntry entry : imageEntries){
                    Movie movie = new Movie();
                    movie.setId(entry.getMovieid());
                    movie.setOverview(entry.getOverview());
                    movie.setOriginalTitle(entry.getTitle());
                    movie.setPosterPath(entry.getPosterpath());
                    movie.setVoteAverage(entry.getUserrating());
                    movie.setReleaseDate(entry.getReleasedate());
                    movies.add(movie);
                }

                adapter.setMovies(movies);
            }
        });
    }





}