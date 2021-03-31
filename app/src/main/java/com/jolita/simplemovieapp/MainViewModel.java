package com.jolita.simplemovieapp;

import android.app.Application;
import android.util.Log;

import com.jolita.simplemovieapp.database.AppDatabase;
import com.jolita.simplemovieapp.database.FavoriteEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavoriteEntry>> favorites;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favorites = database.favoriteDao().loadAllFavorite();
    }

    public LiveData<List<FavoriteEntry>> getFavorite() {
        return favorites;
    }


}
