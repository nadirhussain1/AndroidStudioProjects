package com.brainpixel.deliveryapp.model;

/**
 * Created by nadirhussain on 02/09/2018.
 */

public class Rating {

    private String itemId;
    private String userId;
    private String title;
    private String review;
    private String reviewerName;
    private String ratingStars;
    private String reviewDate;
    private String isFullReview;
    private String isFullReviewApproved;

    public Rating(){
        ratingStars="";
        isFullReview="no";
        isFullReviewApproved="no";
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(String ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getIsFullReview() {
        return isFullReview;
    }

    public void setIsFullReview(String isFullReview) {
        this.isFullReview = isFullReview;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsFullReviewApproved() {
        return isFullReviewApproved;
    }

    public void setIsFullReviewApproved(String isFullReviewApproved) {
        this.isFullReviewApproved = isFullReviewApproved;
    }

}
