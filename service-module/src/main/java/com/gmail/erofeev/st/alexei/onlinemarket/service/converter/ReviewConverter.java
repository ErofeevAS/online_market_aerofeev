package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;

import java.util.List;

public interface ReviewConverter {
    ReviewDTO toDTO(Review review);

    Review fromDTO(ReviewDTO reviewDTO);

    List<ReviewDTO> toListDTO(List<Review> reviews);
}
