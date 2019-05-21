package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public void updateHiddenFieldsByIds(Connection connection, Map<Long, Boolean> hiddenForId) {
        String sql = "UPDATE review SET hidden = ? WHERE id = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Long id : hiddenForId.keySet()) {
                boolean hided = hiddenForId.get(id);
                preparedStatement.setBoolean(1, hided);
                preparedStatement.setLong(2, id);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during updating hided field in reviews with ids: %s", hiddenForId), e);
        }
    }
}
