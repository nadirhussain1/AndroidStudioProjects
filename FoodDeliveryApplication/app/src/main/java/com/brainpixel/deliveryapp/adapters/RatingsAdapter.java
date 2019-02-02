package com.brainpixel.deliveryapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.model.Rating;
import com.brainpixel.deliveryapp.utils.ScalingUtility;

import java.util.List;

/**
 * Created by nadirhussain on 09/09/2018.
 */

public class RatingsAdapter extends Adapter<RatingsAdapter.RatingsViewHolder> {
    private Activity activity;
    private List<Rating> ratings;

    public RatingsAdapter(Activity activity, List<Rating> ratings) {
        this.activity = activity;
        this.ratings = ratings;
    }

    @Override
    public RatingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.item_review, null);
        new ScalingUtility(activity).scaleRootView(view);
        return new RatingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingsViewHolder holder, int position) {
        Rating ratingItem = ratings.get(position);
        holder.itemRatingBar.setRating(Float.valueOf(ratingItem.getRatingStars()));
        holder.dateTextView.setText(ratingItem.getReviewDate());
        holder.titleTextView.setText(ratingItem.getTitle());
        holder.reviewerNameTextView.setText(ratingItem.getReviewerName());
        holder.reviewDetailsTextView.setText(ratingItem.getReview());
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class RatingsViewHolder extends ViewHolder {
        public RatingBar itemRatingBar;
        public TextView dateTextView, titleTextView, reviewerNameTextView, reviewDetailsTextView;

        public RatingsViewHolder(View itemView) {
            super(itemView);

            itemRatingBar = itemView.findViewById(R.id.itemRatingBar);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            reviewerNameTextView = itemView.findViewById(R.id.reviewNameTextView);
            reviewDetailsTextView = itemView.findViewById(R.id.reviewDetailTextView);
        }
    }
}
