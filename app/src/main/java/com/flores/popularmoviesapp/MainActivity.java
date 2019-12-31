package com.flores.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.database.Movie;
import com.flores.popularmoviesapp.util.InjectorUtils;
import com.flores.popularmoviesapp.util.NetworkUtils;
import com.flores.popularmoviesapp.viewmodel.MainActivityViewModel;
import com.flores.popularmoviesapp.viewmodel.MainViewModelFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, movies -> {
            mMovieAdapter.setMovieData(movies);
            if (movies != null && movies.size() != 0) showMovieDataView();
            else showLoading();
        });

        Log.d(LOG_TAG, "Main activity created");
    }

    private void showMovieDataView() {
        Log.d(LOG_TAG, "showMovieDataView");
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(LOG_TAG, "onClick: " + movie.getId());
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        intentToStartDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
        startActivity(intentToStartDetailActivity);
    }

    private void showLoading() {
        Log.d(LOG_TAG, "Loading");
        // Then, hide the weather data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            mViewModel.fetchMovies(NetworkUtils.Sort.POPULAR.name());
            return true;
        }

        if (id == R.id.action_sort_top_rated) {
            mViewModel.fetchMovies(NetworkUtils.Sort.TOP_RATED.name());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }
}
