package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;

import java.sql.Connection;
import java.util.Map;

public interface ReviewRepository extends GenericRepository<Long, Review> {
    void updateHidedFields(Connection connection, Map<Long, Boolean> mapIdHided);
}
