package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;

import java.util.List;

public interface ReviewService {
    Integer getAmount(int amountOfDisplayedReview);

    List<ReviewDTO> getReviews(int page, int amount);

    void delete(Long id);

    void updateDifference(ReviewsListWrapper reviewsListWrapper, ReviewsListWrapper reviewsChanges);
}
