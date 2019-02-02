package com.brainpixel.deliveryapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainpixel.deliveryapp.R;
import com.brainpixel.deliveryapp.managers.GlobalDataManager;
import com.brainpixel.deliveryapp.model.MainItem;
import com.brainpixel.deliveryapp.model.Rating;
import com.brainpixel.deliveryapp.utils.GlobalConstants;
import com.brainpixel.deliveryapp.utils.GlobalUtil;
import com.brainpixel.deliveryapp.utils.ScalingUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nadirhussain on 09/09/2018.
 */

public class WriteReviewActivity extends Activity {
    @BindView(R.id.itemNameView)
    TextView itemNameView;
    @BindView(R.id.itemPriceTextView)
    TextView itemPriceTextView;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.fullReviewCheckBox)
    CheckBox fullReviewCheckBox;
    @BindView(R.id.publisherEditor)
    EditText publisherEditor;
    @BindView(R.id.titleEditor)
    EditText titleEditor;
    @BindView(R.id.reviewEditor)
    EditText reviewEditor;
    @BindView(R.id.fullReviewWriteFieldsLayout)
    LinearLayout fullReviewWriteFieldsLayout;
    @BindView(R.id.sendReviewTextView)
    TextView sendReviewButton;
    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    private MainItem selectedItem;
    private Rating ratingObject = new Rating();
    private DocumentReference documentReference = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
        ButterKnife.bind(this);

        selectedItem = GlobalDataManager.getInstance().selectedItem;
        checkExistingRating();
    }

    private void attachView() {
        View view = LayoutInflater.from(this).inflate(R.layout.write_review_screen, null);
        new ScalingUtility(this).scaleRootView(view);
        setContentView(view);
    }

    private void onExistingRatingFetched() {
        itemNameView.setText(selectedItem.getName());
        itemPriceTextView.setText("Rs." + selectedItem.getPrice());
        fullReviewCheckBox.setOnCheckedChangeListener(fullReviewCheckChangeListener);

        if (documentReference != null) {
            sendReviewButton.setText("Update Review");
            ratingBar.setRating(GlobalUtil.getFloatValue(ratingObject.getRatingStars()));
            if (GlobalUtil.getValue(ratingObject.getIsFullReview()).equalsIgnoreCase("yes")) {
                fullReviewCheckBox.setChecked(true);
                publisherEditor.setText(ratingObject.getReviewerName());
                titleEditor.setText(ratingObject.getTitle());
                reviewEditor.setText(ratingObject.getReview());
            }
        }


    }
    private void showProgressBarLayout() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarLayout() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @OnClick(R.id.backIconView)
    public void onArrowClicked() {
        goBack();
    }

    @OnClick(R.id.titleView)
    public void onTitleClicked() {
        goBack();
    }

    @OnClick(R.id.sendReviewTextView)
    public void sendReviewClick() {
        if (isValid()) {
            showProgressBarLayout();

            float newRatedValue = ratingBar.getRating();
            String publisher = publisherEditor.getText().toString().trim();
            String title = titleEditor.getText().toString().trim();
            String review = reviewEditor.getText().toString().trim();
            String todayDate = "" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


            ratingObject.setItemId(selectedItem.getItemId());
            ratingObject.setRatingStars("" + newRatedValue);
            ratingObject.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            if (fullReviewCheckBox.isChecked()) {
                ratingObject.setIsFullReview("yes");
                ratingObject.setIsFullReviewApproved("no");
                ratingObject.setReview(review);
                ratingObject.setReviewerName(publisher);
                ratingObject.setTitle(title);
                ratingObject.setReviewDate(todayDate);

            }
            updateRatingsInFirebase(ratingObject, selectedItem);

        }
    }

    private boolean isValid() {
        float rating = ratingBar.getRating();
        if (rating == 0) {
            GlobalUtil.showToastMessage(this, getString(R.string.no_star_clicked_msg), GlobalConstants.TOAST_RED);
            return false;
        }

        if (fullReviewCheckBox.isChecked()) {
            String publisher = publisherEditor.getText().toString().trim();
            String title = titleEditor.getText().toString().trim();
            String review = reviewEditor.getText().toString().trim();

            if (publisher.isEmpty()) {
                GlobalUtil.showToastMessage(this, getString(R.string.empty_publisher_msg), GlobalConstants.TOAST_RED);
                return false;
            }
            if (title.isEmpty()) {
                GlobalUtil.showToastMessage(this, getString(R.string.empty_title_msg), GlobalConstants.TOAST_RED);
                return false;
            }
            if (review.isEmpty()) {
                GlobalUtil.showToastMessage(this, getString(R.string.blank_comment_msg), GlobalConstants.TOAST_RED);
                return false;
            }
        }
        return true;
    }

    private OnCheckedChangeListener fullReviewCheckChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                fullReviewWriteFieldsLayout.setVisibility(View.VISIBLE);
            } else {
                fullReviewWriteFieldsLayout.setVisibility(View.GONE);
            }
        }
    };

    private void checkExistingRating() {
        showProgressBarLayout();
        FirebaseFirestore.getInstance().collection("ratings")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("itemId", selectedItem.getItemId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideProgressBarLayout();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (task.isSuccessful() && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            documentReference = documentSnapshot.getReference();
                            ratingObject = documentSnapshot.toObject(Rating.class);
                        }
                        onExistingRatingFetched();
                    }
                });
    }


    private void updateRatingsInFirebase(final Rating rating, final MainItem selectedItem) {
        if (documentReference != null) {
            updateExistingRatingObject(documentReference, rating, selectedItem);
        } else {
            addNewRating(rating, selectedItem);
        }
    }

    private void updateExistingRatingObject(DocumentReference documentReference, final Rating rating, final MainItem selectedItem) {
        documentReference.update("ratingStars", rating.getRatingStars());
        documentReference.update("title", rating.getTitle());
        documentReference.update("review", rating.getReview());
        documentReference.update("reviewerName", rating.getReviewerName());
        documentReference.update("reviewDate", rating.getReviewDate());
        documentReference.update("isFullReview", rating.getIsFullReview()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateAverageRatingAndCount(selectedItem, rating.getRatingStars(), false);
                } else {
                    showCompletionMessage(false);
                }
            }
        });
    }

    private void addNewRating(final Rating rating, final MainItem selectedItem) {
        FirebaseFirestore.getInstance().collection("ratings").add(rating).addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    updateAverageRatingAndCount(selectedItem, rating.getRatingStars(), true);
                } else {
                    showCompletionMessage(false);
                }
            }
        });
    }


    private void updateAverageRatingAndCount(MainItem selectedItem, String newRating, boolean isNewRating) {
        float previousRating = Float.valueOf(selectedItem.getAvgRating());
        float newRatingValue = Float.valueOf(newRating);
        int previousCount = Integer.valueOf(selectedItem.getTotalRatings());
        int newCount = previousCount + 1;

        float newAverageRating = previousRating + ((newRatingValue - previousRating) / newCount);
        selectedItem.setAvgRating("" + newAverageRating);
        if (isNewRating) {
            selectedItem.setTotalRatings("" + newCount);
        } else if (previousCount == 1) {
            selectedItem.setAvgRating("" + newRating);
        }


        DocumentReference document = FirebaseFirestore.getInstance().collection("items").document(selectedItem.getMainItemDocumentId());
        document.update("avgRating", selectedItem.getAvgRating());
        document.update("totalRatings", selectedItem.getTotalRatings()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBarLayout();
                showCompletionMessage(task.isSuccessful());
            }
        });
    }


    public void showCompletionMessage(boolean isSuccess) {
        if (isSuccess) {
            GlobalUtil.showToastMessage(this, getString(R.string.review_submitted_msg), GlobalConstants.TOAST_DARK);
        } else {
            GlobalUtil.showToastMessage(this, getString(R.string.review_failed_msg), GlobalConstants.TOAST_RED);
        }

    }

    private void goBack() {
        finish();
    }


}
