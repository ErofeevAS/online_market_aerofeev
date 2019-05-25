package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;

public interface ReviewRepository extends GenericRepository<Long, Review> {
    void updateHiddenById(Long id, Boolean hidden);
}
