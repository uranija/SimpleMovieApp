package com.jolita.simplemovieapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteEntry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "movieid")
    private int movieid;

    @ColumnInfo(name = "posterpath")
    private String posterpath;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "userrating")
    private Double userrating;

    @ColumnInfo(name = "releasedate")
    private String releasedate;

    @ColumnInfo(name = "overview")
    private String overview;


    @Ignore
    public FavoriteEntry(int movieid, String posterpath, String title,Double userrating,String releasedate,String overview ) {
        this.movieid = movieid;
        this.posterpath = posterpath;
        this.title = title;
        this.userrating=userrating;
        this.releasedate=releasedate;
        this.overview=overview;
    }

    public FavoriteEntry(int id,int movieid, String posterpath, String title,Double userrating, String releasedate, String overview ) {
        this.id = id;
        this.movieid = movieid;
        this.posterpath = posterpath;
        this.title = title;
        this.userrating=userrating;
        this.releasedate=releasedate;
        this.overview=overview;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getUserrating() {
        return userrating;
    }

    public void setUserrating(double userrating) {
        this.userrating = userrating;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


}






