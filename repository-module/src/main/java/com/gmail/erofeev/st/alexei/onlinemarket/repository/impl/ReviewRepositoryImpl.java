package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ReviewRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Review;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl implements ReviewRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public List<Review> getReviews(Connection connection, int offset, int amount) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT reviews.id as review_id,content,date,reviews.deleted as review_deleted,hided,user_id,lastname,firstname,patronymic" +
                " FROM reviews" +
                "  JOIN users on reviews.user_id=users.id where reviews.deleted=false LIMIT ?,? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review review = getReview(resultSet);
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException("Can't get reviews", e);
        }
    }

    @Override
    public void updateHidedFields(Connection connection, Map<Long, Boolean> mapIdHided) {
        String sql = "UPDATE reviews SET hided = ? WHERE id = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Long id : mapIdHided.keySet()) {
                boolean hided = mapIdHided.get(id);
                preparedStatement.setBoolean(1, hided);
                preparedStatement.setLong(2, id);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during updating hided field in reviews with ids: %s", mapIdHided), e);
        }
    }

    @Override
    public void delete(Connection connection, Long id) {
        String sql = "UPDATE reviews SET deleted=true WHERE id =? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during deleting a reviews with id: %s", id), e);
        }
    }

    private Review getReview(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("review_id");
        Timestamp date = resultSet.getTimestamp("date");
        String content = resultSet.getString("content");
        boolean reviewDeleted = resultSet.getBoolean("review_deleted");
        boolean hided = resultSet.getBoolean("hided");
        long userId = resultSet.getLong("user_id");
        String lastName = resultSet.getString("lastname");
        String firstName = resultSet.getString("firstname");
        String patronymic = resultSet.getString("patronymic");
        User user = new User();
        user.setId(userId);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPatronymic(patronymic);
        return new Review(id, user, content, date, reviewDeleted, hided);
    }
}
