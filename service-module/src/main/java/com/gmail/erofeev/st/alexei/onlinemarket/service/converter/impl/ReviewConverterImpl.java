package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ReviewConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    private final UserConverter userConverter;

    @Autowired
    public ReviewConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        Long id = review.getId();
        UserDTO user = userConverter.toDTO(review.getUser());
        String content = review.getContent();
        Timestamp date = review.getDate();
        Boolean deleted = review.getDeleted();
        Boolean hided = review.getHided();
        return new ReviewDTO(id, user, content, date, deleted, hided);
    }

    @Override
    public Review fromDTO(ReviewDTO reviewDTO) {
        Long id = reviewDTO.getId();
        Boolean deleted = reviewDTO.getDeleted();
        Boolean hided = reviewDTO.getHided();
        return new Review(id, deleted, hided);
    }

    @Override
    public List<ReviewDTO> toListDTO(List<Review> reviews) {
        List<ReviewDTO> articleDTOList = new ArrayList<>();
        for (Review review : reviews) {
            articleDTOList.add(toDTO(review));
        }
        return articleDTOList;
    }
}
