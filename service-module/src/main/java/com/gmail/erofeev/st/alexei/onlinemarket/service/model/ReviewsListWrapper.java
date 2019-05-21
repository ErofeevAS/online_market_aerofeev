package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.util.ArrayList;
import java.util.List;

public class ReviewsListWrapper {
    private List<ReviewDTO> reviews;

    public ReviewsListWrapper(List<ReviewDTO> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }

    public ReviewsListWrapper() {
        this.reviews = new ArrayList<>();
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }
}