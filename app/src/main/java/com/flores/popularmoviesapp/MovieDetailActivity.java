package com.flores.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flores.popularmoviesapp.data.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "com.flores.popularmoviesapp.data.Movie";

    private Movie mMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mSynopsis;
    private ImageView mImagePoster;

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

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE)) {
                mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(EXTRA_MOVIE);
                if (mMovie != null) {
                    mTitle.setText(mMovie.getTitle());
                    mReleaseDate.setText(mMovie.getReleaseDate());
                    mVoteAverage.setText(Double.toString(mMovie.getVoteAverage()));
                    mSynopsis.setText(mMovie.getSynopsis());

                    Picasso.get()
                            .load(mMovie.getPoster())
                            .into(mImagePoster);
                }
            }
        }
    }
}
