package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ReviewRepository extends GenericRepository {
    List<Review> getReviews(Connection connection, int offset, int amount);

    void updateHidedFields(Connection connection, Map<Long,Boolean> mapIdHided);

    void delete(Connection connection, Long id);
}
