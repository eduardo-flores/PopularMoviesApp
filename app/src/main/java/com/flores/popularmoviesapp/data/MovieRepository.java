/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flores.popularmoviesapp.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.flores.popularmoviesapp.data.database.Movie;
import com.flores.popularmoviesapp.data.database.MovieDao;
import com.flores.popularmoviesapp.data.network.MovieNetworkDataSource;
import com.flores.popularmoviesapp.util.AppExecutors;

import java.util.List;

public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieRepository sInstance;
    private final MovieDao mMovieDao;
    private final MovieNetworkDataSource mMovieNetworkDataSource;
    private final AppExecutors mExecutors;

    private MovieRepository(MovieDao movieDao,
                            MovieNetworkDataSource weatherNetworkDataSource,
                            AppExecutors executors) {
        mMovieDao = movieDao;
        mMovieNetworkDataSource = weatherNetworkDataSource;
        mExecutors = executors;
    }

    public synchronized static MovieRepository getInstance(
            MovieDao movieDao, MovieNetworkDataSource weatherNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieRepository(movieDao, weatherNetworkDataSource, executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<Boolean> isFavorite(int movieId) {
        return mMovieDao.isFavorite(movieId);
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieNetworkDataSource.getMovies();
    }

    public LiveData<List<Trailer>> getTrailers() {
        return mMovieNetworkDataSource.getTrailers();
    }

    public LiveData<List<Review>> getReviews() {
        return mMovieNetworkDataSource.getReviews();
    }

    public LiveData<List<Movie>> getMoviesFavorite() {
        return mMovieDao.getMoviesFavorite();
    }

    public void deleteFavorite(int movieId) {
        mExecutors.diskIO().execute(() -> mMovieDao.delete(movieId));
    }

    public void addFavorite(Movie movie) {
        mExecutors.diskIO().execute(() -> mMovieDao.insert(movie));
    }

    public void fetchMovies(String sortName) {
        mMovieNetworkDataSource.fetchMovies(sortName);
    }

    public void fetchTrailers(String movieId) {
        mMovieNetworkDataSource.fetchTrailers(movieId);
    }

    public void fetchReviews(String movieId) {
        mMovieNetworkDataSource.fetchReviews(movieId);
    }

}