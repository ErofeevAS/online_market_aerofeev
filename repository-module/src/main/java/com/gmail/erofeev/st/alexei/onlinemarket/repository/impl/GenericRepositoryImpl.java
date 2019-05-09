package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.GenericRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class GenericRepositoryImpl implements GenericRepository {
    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Integer getAmount(Connection connection, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE deleted = false";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            String message = "Can't get amount of  " + tableName + " " + e.getMessage();
            logger.error(message, e);
            throw new RepositoryException(message, e);
        }
    }

}
