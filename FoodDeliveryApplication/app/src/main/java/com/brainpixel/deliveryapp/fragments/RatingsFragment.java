package com.brainpixel.deliveryapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.activities.WriteReviewActivity;
import com.brainpixel.deliveryapp.adapters.RatingsAdapter;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.model.Rating;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.brainpixel.deliveryapp.utils.VerticalSpaceItemDecoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 08/09/2018.
 */

public class RatingsFragment extends Fragment {
    private MainItem selectedItem;

    @BindView(R.id.ratingsInfoHeadTextView)
    TextView ratingsInfoHeadTextView;
    @BindView(R.id.avgRatingValueTextView)
    TextView avgRatingValueTextView;
    @BindView(R.id.fromStaticTextView)
    TextView fromCustomersTextView;
    @BindView(R.id.userReviewsRecyclerView)
    RecyclerView userReviewsRecyclerView;
    @BindView(R.id.noReviewMsgTextView)
    TextView noReviewMsgView;
    @BindView(R.id.loadingBar)
    ProgressBar loadingBar;

    private List<Rating> fullReviewsRatingList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = attachViews(inflater);
        selectedItem = GlobalDataManager.getInstance().selectedItem;
        return view;
    }

    private View attachViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.tab_reviews, null);
        new ScalingUtility(getActivity()).scaleRootView(view);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindValuesWithViews();
    }

    private void bindValuesWithViews() {
        if (selectedItem != null) {
            ratingsInfoHeadTextView.setText("RATINGS (" + selectedItem.getTotalRatings() + ")");
            float ratingValue = Float.valueOf(selectedItem.getAvgRating());
            String formattedRating = String.format("%.2f", ratingValue);
            avgRatingValueTextView.setText(formattedRating + " / " + "5");
            fromCustomersTextView.setText("from " + selectedItem.getTotalRatings() + " customers");

            fetchRatings();
        }
    }

    @OnClick(R.id.writeReviewTextView)
    public void writeReviewClicked() {
        Intent intent = new Intent(getActivity(), WriteReviewActivity.class);
        startActivity(intent);
    }

    private void showRatingsInRecyclerView() {
        RatingsAdapter ratingsAdapter = new RatingsAdapter(getActivity(), fullReviewsRatingList);

        int space = (int) ScalingUtility.resizeDimension(10);
        userReviewsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(space));
        userReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userReviewsRecyclerView.setAdapter(ratingsAdapter);

        userReviewsRecyclerView.setVisibility(View.VISIBLE);
        noReviewMsgView.setVisibility(View.GONE);
    }

    private void onNoFullReviews() {
        userReviewsRecyclerView.setVisibility(View.GONE);
        noReviewMsgView.setVisibility(View.VISIBLE);
    }


    private void fetchRatings() {
        loadingBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("ratings")
                .whereEqualTo("itemId", selectedItem.getItemId())
                .whereEqualTo("isFullReview", "yes")
                .whereEqualTo("isFullReviewApproved", "yes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        loadingBar.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            fullReviewsRatingList = queryDocumentSnapshots.toObjects(Rating.class);
                            showRatingsInRecyclerView();
                        } else {
                            onNoFullReviews();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.setVisibility(View.GONE);
                        onNoFullReviews();
                    }
                });
    }


}
