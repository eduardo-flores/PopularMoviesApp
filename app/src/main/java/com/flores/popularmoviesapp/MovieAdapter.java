package com.flores.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.Movie;
import com.flores.popularmoviesapp.util.MovieJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * {@link MovieAdapter} exposes a list of movies a
 * {@link androidx.recyclerview.widget.RecyclerView}
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final MovieAdapterOnClickHandler mClickHandler;
    private String[] mMovieData;

    MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieJson = mMovieData[position];

        try {
            Movie movie = MovieJsonUtils.getMovieFromJson(movieJson);

            Picasso.get()
                    .load(movie.getPoster())
                    .into(movieAdapterViewHolder.mMovieImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


    void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ImageView mMovieImageView;

        MovieAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movieJson = mMovieData[adapterPosition];
            try {
                Movie movie = MovieJsonUtils.getMovieFromJson(movieJson);

                mClickHandler.onClick(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}