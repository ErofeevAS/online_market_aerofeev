package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ProfileRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ProfileRepositoryImpl extends GenericRepositoryImpl<Long, Profile> implements ProfileRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProfileRepositoryImpl.class);

    @Override
    public void save(Connection connection, Profile profile) {
        Long id = profile.getId();
        String sql = "INSERT INTO profile (user_id) VALUES(?) ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during saving profile: %s :", profile, e));
        }
    }
}