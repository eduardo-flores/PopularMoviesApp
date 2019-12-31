package com.flores.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.flores.popularmoviesapp.data.Trailer;
import com.flores.popularmoviesapp.data.database.Movie;
import com.flores.popularmoviesapp.databinding.ActivityMovieDetailBinding;
import com.flores.popularmoviesapp.util.InjectorUtils;
import com.flores.popularmoviesapp.util.NetworkUtils;
import com.flores.popularmoviesapp.viewmodel.MovieDetailActivityViewModel;
import com.flores.popularmoviesapp.viewmodel.MovieDetailViewModelFactory;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    public static final String EXTRA_MOVIE = "com.flores.popularmoviesapp.data.database.Movie";
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private MovieDetailActivityViewModel mViewModel;

    private ActivityMovieDetailBinding mDetailBinding;

    private Movie mMovie;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mDetailBinding.rvTrailers.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mDetailBinding.rvTrailers.setAdapter(mTrailerAdapter);

        mDetailBinding.rvReviews.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mDetailBinding.rvReviews.setAdapter(mReviewAdapter);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(EXTRA_MOVIE);
                if (mMovie != null) {
                    MovieDetailViewModelFactory factory = InjectorUtils.provideMovieDetailViewModelFactory(this.getApplicationContext(), mMovie.getId());
                    mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailActivityViewModel.class);

                    bindToUI();

                    loadTrailerData();

                    loadReviewData();
                }
            }
        }
    }

    private void bindToUI() {
        Log.d(LOG_TAG, "bindToUI");
        mDetailBinding.tvDetailTitle.setText(mMovie.getTitle());
        mDetailBinding.tvDetailReleaseDate.setText(mMovie.getReleaseDate());
        mDetailBinding.tvDetailVoteAverage.setText(String.format(
                getApplicationContext().getString(R.string.vote_average_format),
                mMovie.getVoteAverage()
        ));
        mDetailBinding.tvDetailSynopsis.setText(mMovie.getSynopsis());
        mDetailBinding.ivDetailMoviePoster.setContentDescription(mMovie.getTitle());
        mDetailBinding.ivFavorite.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Movie favorite clicked: " + mMovie.getId());
            mViewModel.setFavorite(mMovie);
        });


        Picasso.get()
                .load(mMovie.getPoster())
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mDetailBinding.ivDetailMoviePoster);

        mViewModel.isFavorite().observe(this, isFavorite -> {
            Log.d(LOG_TAG, "Movie isFavorite: " + isFavorite);
            if (isFavorite) {
                mDetailBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                mDetailBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_border_black_48dp);
            }
            mDetailBinding.ivFavorite.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClick(Trailer trailer) {
        Uri youtubeUri = NetworkUtils.buildWatchUrl(trailer.getKey());

        Intent intentToWatchTrailer = new Intent(Intent.ACTION_VIEW, youtubeUri);
        startActivity(intentToWatchTrailer);
    }

    private void loadTrailerData() {
        mViewModel.getTrailers().observe(this, trailers -> {
            mTrailerAdapter.setTrailerData(trailers);
            if (trailers != null && trailers.size() != 0) showTrailerDataView();
            else mDetailBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
        });
    }

    private void showTrailerDataView() {
        mDetailBinding.rvTrailers.setVisibility(View.VISIBLE);
    }

    private void loadReviewData() {
        mViewModel.getReviews().observe(this, reviews -> {
            mReviewAdapter.setReviewData(reviews);
            if (reviews != null && reviews.size() != 0) showReviewDataView();
            else mDetailBinding.pbLoadingIndicatorReview.setVisibility(View.VISIBLE);
        });
    }

    private void showReviewDataView() {
        mDetailBinding.rvReviews.setVisibility(View.VISIBLE);
    }
}
