package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ReviewService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ReviewConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewConverter reviewConverter) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
    }

    @Override
    public Integer getAmount(int amountOfDisplayedReview) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer amount = reviewRepository.getAmount(connection, "reviews");
                amount = (Math.round(amount / amountOfDisplayedReview) + 1);
                connection.commit();
                return amount;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException("Can't get amount of reviews from repository.", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public List<ReviewDTO> getReviews(int page, int amount) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int offset = (page - 1) * amount;
                List<Review> reviews = reviewRepository.getReviews(connection, offset, amount);
                List<ReviewDTO> reviewDTOList = reviewConverter.toListDTO(reviews);
                connection.commit();
                return reviewDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException("Can't get reviews from repository.", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                reviewRepository.delete(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't delete review with id: %s :", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public void updateHidedFields(ReviewsListWrapper newReviewsList) {
        Map<Long, Boolean> mapIdHided = getMapIdHided(newReviewsList.getReviews());
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                reviewRepository.updateHidedFields(connection, mapIdHided);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't update  hided fields for reviews with ids: %s", mapIdHided.keySet()), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    private Map<Long, Boolean> getMapIdHided(List<ReviewDTO> reviewsDTO) {
        Map<Long, Boolean> reviewIdHidedMap = new HashMap<>();
        for (ReviewDTO review : reviewsDTO) {
            Long id = review.getId();
            Boolean hided = review.getHided();
            if (hided == null) {
                hided = false;
            }
            reviewIdHidedMap.put(id, hided);
        }
        return reviewIdHidedMap;
    }
}
