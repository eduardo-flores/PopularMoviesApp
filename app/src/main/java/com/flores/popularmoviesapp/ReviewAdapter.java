package com.flores.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flores.popularmoviesapp.data.Review;

import java.util.List;

/**
 * {@link ReviewAdapter} exposes a list of movies a
 * {@link RecyclerView}
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Review> mReviewData;

    ReviewAdapter() {
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        Review review = mReviewData.get(position);

        reviewAdapterViewHolder.mReviewAuthor.setText(review.getAuthor());
        reviewAdapterViewHolder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }


    void setReviewData(List<Review> reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mReviewAuthor;
        final TextView mReviewContent;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.tv_review_author);
            mReviewContent = view.findViewById(R.id.tv_review_content);
        }
    }
}