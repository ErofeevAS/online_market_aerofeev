package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ReviewConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    private final UserConverter userConverter;

    @Autowired
    public ReviewConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setCreatedDate(review.getCreatedDate());
        reviewDTO.setDeleted(review.getDeleted());
        reviewDTO.setHidden(review.getHidden());
        UserDTO user = userConverter.toDTO(review.getUser());
        reviewDTO.setUser(user);
        return reviewDTO;
    }

    @Override
    public Review fromDTO(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setDeleted(reviewDTO.getDeleted());
        review.setHidden(reviewDTO.getHidden());
        return review;
    }

    @Override
    public List<ReviewDTO> toListDTO(List<Review> reviews) {
        return reviews.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
