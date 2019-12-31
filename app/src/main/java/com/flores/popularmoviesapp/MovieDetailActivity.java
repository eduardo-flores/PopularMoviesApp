package com.flores.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.Trailer;
import com.flores.popularmoviesapp.data.database.Movie;
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

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private RecyclerView mRecyclerViewReview;
    private TextView mErrorMessageDisplayReview;
    private ProgressBar mLoadingIndicatorReview;
    private MovieDetailActivityViewModel mViewModel;

    private Movie mMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mSynopsis;
    private ImageView mImagePoster;
    private ImageView mImageFavorite;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitle = findViewById(R.id.tv_detail_title);
        mReleaseDate = findViewById(R.id.tv_detail_release_date);
        mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        mSynopsis = findViewById(R.id.tv_detail_synopsis);
        mImagePoster = findViewById(R.id.iv_detail_movie_poster);
        mImageFavorite = findViewById(R.id.iv_favorite);
        mImageFavorite.setOnClickListener(view -> {
            Log.d(LOG_TAG, "Movie favorite clicked: " + mMovie.getId());
            mViewModel.setFavorite(mMovie);
        });

        mRecyclerView = findViewById(R.id.rv_trailers);
        mRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerViewReview = findViewById(R.id.rv_reviews);
        mRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerViewReview.setAdapter(mReviewAdapter);

        mErrorMessageDisplayReview = findViewById(R.id.tv_error_message_display_review);

        mLoadingIndicatorReview = findViewById(R.id.pb_loading_indicator_review);

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
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(String.format(
                getApplicationContext().getString(R.string.vote_average_format),
                mMovie.getVoteAverage()
        ));
        mSynopsis.setText(mMovie.getSynopsis());
        mImagePoster.setContentDescription(mMovie.getTitle());

        Picasso.get()
                .load(mMovie.getPoster())
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(mImagePoster);

        mViewModel.isFavorite().observe(this, isFavorite -> {
            Log.d(LOG_TAG, "Movie isFavorite: " + isFavorite);
            if (isFavorite) {
                mImageFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                mImageFavorite.setImageResource(R.drawable.ic_favorite_border_black_48dp);
            }
            mImageFavorite.setVisibility(View.VISIBLE);
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
            else mLoadingIndicator.setVisibility(View.VISIBLE);
        });
    }

    private void showTrailerDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadReviewData() {
        mViewModel.getReviews().observe(this, reviews -> {
            mReviewAdapter.setReviewData(reviews);
            if (reviews != null && reviews.size() != 0) showReviewDataView();
            else mLoadingIndicatorReview.setVisibility(View.VISIBLE);
        });
    }

    private void showReviewDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplayReview.setVisibility(View.INVISIBLE);
        /* Then, make sure the trailer data is visible */
        mRecyclerViewReview.setVisibility(View.VISIBLE);
    }
}
