package com.ftohbackend.dto;

public class RatingDTO {

    private Integer ratingId;
    private Integer customerId;
    private Integer productId;
    private Integer rating;
    private String feedback;

    public RatingDTO() {
    }

    public RatingDTO(Integer ratingId, Integer customerId, Integer productId, Integer rating, String feedback) {
        this.ratingId = ratingId;
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.feedback = feedback;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "RatingDTO [ratingId=" + ratingId + ", customerId=" + customerId + ", productId=" + productId +
                ", rating=" + rating + ", feedback=" + feedback + "]";
    }
}
