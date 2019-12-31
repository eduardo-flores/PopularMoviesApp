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

package com.flores.popularmoviesapp.util;

import android.content.Context;

import com.flores.popularmoviesapp.data.MovieRepository;
import com.flores.popularmoviesapp.data.database.MovieDatabase;
import com.flores.popularmoviesapp.data.network.MovieNetworkDataSource;
import com.flores.popularmoviesapp.viewmodel.MainViewModelFactory;
import com.flores.popularmoviesapp.viewmodel.MovieDetailViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Movie
 */
public class InjectorUtils {

    private static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieNetworkDataSource networkDataSource = MovieNetworkDataSource.getInstance(executors);
        return MovieRepository.getInstance(database.movieDao(), networkDataSource, executors);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static MovieDetailViewModelFactory provideMovieDetailViewModelFactory(Context context, int movieId) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MovieDetailViewModelFactory(repository, movieId);
    }
}