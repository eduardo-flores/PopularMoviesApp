package com.flores.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.database.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link MovieAdapter} exposes a list of movies a
 * {@link androidx.recyclerview.widget.RecyclerView}
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final MovieAdapterOnClickHandler mClickHandler;
    private List<Movie> mMovieData;

    MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movie = mMovieData.get(position);
        movieAdapterViewHolder.mMovieImageView.setContentDescription(movie.getTitle());

        Picasso.get()
                .load(movie.getPoster())
                .placeholder(R.drawable.ic_image_black_48dp)
                .into(movieAdapterViewHolder.mMovieImageView);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }


    void setMovieData(List<Movie> movieData) {
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
            Movie movie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}