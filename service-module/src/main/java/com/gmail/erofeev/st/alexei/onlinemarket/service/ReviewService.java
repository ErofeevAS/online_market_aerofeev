package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewHideFieldState;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {
    Integer getAmount(int amountOfDisplayedReview);

    List<ReviewDTO> getReviews(int page, int amount);

    List<ReviewHideFieldState> getIdAndHidedState(List<ReviewDTO> reviews);

    void updateHidedFields(List<ReviewHideFieldState> tempArticleHideFieldStates, List<ReviewHideFieldState> newArticleHideFieldStates);

    void delete(Long id);
}
