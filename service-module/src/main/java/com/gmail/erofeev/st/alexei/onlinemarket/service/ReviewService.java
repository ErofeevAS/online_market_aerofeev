package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;

public interface ReviewService {

    PageDTO<ReviewDTO> getReviews(int page, int amount);

    void delete(Long id);

    void updateHidedFields(ReviewsListWrapper reviewsChanges);
}