package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public void updateHiddenById(Long id, Boolean hidden) {
        String query = "update Review r set hidden =:hidden  where r.id = :id";
        Query q = entityManager.createQuery(query);
        q.setParameter("id", id);
        q.setParameter("hidden", hidden);
        q.executeUpdate();
    }
}