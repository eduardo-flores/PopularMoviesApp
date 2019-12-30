package com.flores.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.Trailer;
import com.flores.popularmoviesapp.util.MovieJsonUtils;

import org.json.JSONException;

/**
 * {@link TrailerAdapter} exposes a list of movies a
 * {@link RecyclerView}
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;
    private String[] mTrailerData;

    TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        String trailerJson = mTrailerData[position];

        try {
            Trailer trailer = MovieJsonUtils.getTrailerFromJson(trailerJson);

            trailerAdapterViewHolder.mTrailerName.setText(trailer.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.length;
    }


    void setTrailerData(String[] trailerData) {
        mTrailerData = trailerData;
        notifyDataSetChanged();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView mTrailerName;

        TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerName = view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String trailerData = mTrailerData[adapterPosition];
            try {
                Trailer trailer = MovieJsonUtils.getTrailerFromJson(trailerData);

                mClickHandler.onClick(trailer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}