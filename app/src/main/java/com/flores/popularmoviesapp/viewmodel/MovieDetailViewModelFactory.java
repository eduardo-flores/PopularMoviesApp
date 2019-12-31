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


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.flores.popularmoviesapp.data.MovieRepository;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository mRepository;
    private final int mMovieId;

    public MovieDetailViewModelFactory(MovieRepository repository, int movieId) {
        this.mRepository = repository;
        this.mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailActivityViewModel(mRepository, mMovieId);
    }
}