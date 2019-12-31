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


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.flores.popularmoviesapp.data.MovieRepository;
import com.flores.popularmoviesapp.data.database.Movie;
import com.flores.popularmoviesapp.util.NetworkUtils;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final MovieRepository mRepository;
    private final LiveData<List<Movie>> mMovies;
    private final LiveData<List<Movie>> mMoviesFavorite;
    private boolean showFavorite = false;

    MainActivityViewModel(MovieRepository repository) {
        mRepository = repository;
        fetchMovies(NetworkUtils.Sort.POPULAR.name());
        mMovies = mRepository.getMovies();
        mMoviesFavorite = mRepository.getMoviesFavorite();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public LiveData<List<Movie>> getMoviesFavorite() {
        return mMoviesFavorite;
    }

    public void fetchMovies(String sortName) {
        mRepository.fetchMovies(sortName);
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }
}
