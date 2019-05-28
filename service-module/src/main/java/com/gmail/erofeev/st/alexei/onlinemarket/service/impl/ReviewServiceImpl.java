package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ReviewService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ReviewConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ReviewsListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
    public PageDTO<ReviewDTO> getReviews(int page, int amount) {
        Integer amountOfEntity = reviewRepository.getAmountOfEntity();
        int maxPages = getMaxPages(amount, amountOfEntity);
        int offset = getOffset(page, maxPages, amount);
        List<Review> reviews = reviewRepository.getEntities(offset, amount);
        List<ReviewDTO> reviewDTOList = reviewConverter.toListDTO(reviews);
        PageDTO<ReviewDTO> pageDTO = new PageDTO<>();
        pageDTO.setList(reviewDTOList);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Review review = reviewRepository.findById(id);
        reviewRepository.remove(review);
        logger.debug(String.format("Review with id:%s was deleted", id));
    }

    @Override
    @Transactional
    public void updateHiddenFields(ReviewsListWrapper newReviewsList) {
        Map<Long, Boolean> mapHiddenForId = getMapIdHided(newReviewsList.getReviews());
        for (Long id : mapHiddenForId.keySet()) {
            Boolean hidden = mapHiddenForId.get(id);
            reviewRepository.updateHiddenById(id, hidden);
            logger.debug(String.format("Review with id:%s hidden field now is %s", id, hidden));
        }
    }

    private Map<Long, Boolean> getMapIdHided(List<ReviewDTO> reviewsDTO) {
        Map<Long, Boolean> reviewIdHidedMap = new HashMap<>();
        for (ReviewDTO review : reviewsDTO) {
            Long id = review.getId();
            Boolean hided = review.getHidden();
            if (hided == null) {
                hided = false;
            }
            reviewIdHidedMap.put(id, hided);
        }
        return reviewIdHidedMap;
    }

    private int getMaxPages(int amount, Integer amountOfEntity) {
        return Math.round(amountOfEntity / amount) + 1;
    }

    private int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }
}