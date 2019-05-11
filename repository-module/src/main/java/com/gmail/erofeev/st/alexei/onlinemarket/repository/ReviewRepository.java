package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

public interface ReviewRepository extends GenericRepository {
    List<Review> getReviews(Connection connection, int offset, int amount);

    void updateHided(Connection connection, Set<Long> keySet);

    void delete(Connection connection, Long id);
}
