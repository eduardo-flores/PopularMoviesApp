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

package com.flores.popularmoviesapp.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.flores.popularmoviesapp.data.MovieRepository;
import com.flores.popularmoviesapp.data.Review;
import com.flores.popularmoviesapp.data.Trailer;
import com.flores.popularmoviesapp.data.database.Movie;

import java.util.List;

public class MovieDetailActivityViewModel extends ViewModel {

    private static final String LOG_TAG = MovieDetailActivityViewModel.class.getSimpleName();

    private final MovieRepository mRepository;
    private final LiveData<Boolean> mIsFavorite;
    private final LiveData<List<Trailer>> mTrailers;
    private final LiveData<List<Review>> mReviews;

    MovieDetailActivityViewModel(MovieRepository repository, int movieId) {
        mRepository = repository;
        mIsFavorite = mRepository.isFavorite(movieId);
        mRepository.fetchTrailers(String.valueOf(movieId));
        mRepository.fetchReviews(String.valueOf(movieId));
        mTrailers = mRepository.getTrailers();
        mReviews = mRepository.getReviews();
        Log.d(LOG_TAG, "Created movieId " + movieId);
    }

    public LiveData<Boolean> isFavorite() {
        return mIsFavorite;
    }

    public LiveData<List<Trailer>> getTrailers() {
        return mTrailers;
    }

    public LiveData<List<Review>> getReviews() {
        return mReviews;
    }


    private void deleteFavorite(int movieId) {
        mRepository.deleteFavorite(movieId);
    }

    private void addFavorite(Movie movie) {
        mRepository.addFavorite(movie);
    }

    public void setFavorite(Movie movie) {
        if (movie != null && movie.getId() > 0) {
            // Check if the movie is already favorite
            if (mIsFavorite.getValue() == Boolean.TRUE) {
                deleteFavorite(movie.getId());
            } else {
                addFavorite(movie);
            }
        }
    }
}
