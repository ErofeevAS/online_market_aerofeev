package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    @Override
    public void updateHiddenById(Long id, Boolean hidden) {
        String hql = "update Review r set hidden =:hidden  where r.id = :id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id", id);
        query.setParameter("hidden", hidden);
        query.executeUpdate();
    }
}