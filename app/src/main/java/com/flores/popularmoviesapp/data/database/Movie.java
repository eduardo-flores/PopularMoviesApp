package com.flores.popularmoviesapp.data.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "movie")
public class Movie implements Serializable {

    @PrimaryKey
    private int id;
    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String synopsis;

    @Ignore
    public Movie() {
    }

    public Movie(int id, String title, String releaseDate, String poster, double voteAverage, String synopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
