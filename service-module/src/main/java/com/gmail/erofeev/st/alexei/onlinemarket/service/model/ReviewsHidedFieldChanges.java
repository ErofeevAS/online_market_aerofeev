package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.util.ArrayList;
import java.util.List;

public class ReviewsHidedFieldChanges {
    private List<ReviewDTO> reviews;

    public ReviewsHidedFieldChanges() {
        reviews = new ArrayList<>();
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
