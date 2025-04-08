package com.ftohbackend.dto;

public class RatingDTO {

    private Integer ratingId;
    private Integer customerId;
    private Integer productId;
    private Integer ratingValue;
    private String feedback;

    public RatingDTO() {
    }

    public RatingDTO(Integer ratingId, Integer customerId, Integer productId, Integer ratingValue, String feedback) {
        this.ratingId = ratingId;
        this.customerId = customerId;
        this.productId = productId;
        this.ratingValue = ratingValue;
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

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
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
                ", rating=" + ratingValue + ", feedback=" + feedback + "]";
    }
}
