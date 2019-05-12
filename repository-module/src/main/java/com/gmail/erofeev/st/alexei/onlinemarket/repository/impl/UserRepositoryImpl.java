package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public List<User> getUsers(Connection connection, int offset, int amount) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT users.id as user_id,lastname,firstname,patronymic,email,`password`,deleted, roles.id as role_id,roles.`name` as role_name" +
                " FROM users" +
                " JOIN roles ON users.role_id = roles.id where users.deleted=false ORDER BY lastname,firstname,patronymic LIMIT ?,?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException("Can't get users", e);
        }
    }

    @Override
    public User findUserByEmail(Connection connection, String email) {
        String sql = "SELECT users.id as user_id,lastname,firstname,patronymic,email,password,deleted, roles.id as role_id,roles.name as role_name" +
                " FROM users" +
                " JOIN roles ON users.role_id = roles.id   where users.email=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return getUser(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Can't get user with email: %s :", email + e.getMessage()), e);
        }
    }

    @Override
    public void delete(Connection connection, List<Long> usersIdForDelete) {
        String subSql = getSubString(usersIdForDelete.size());
        String sql = "UPDATE users SET deleted=true WHERE id IN " + subSql + " and undeletable=false ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int counter = 1;
            for (Long id : usersIdForDelete) {
                preparedStatement.setLong(counter, id);
                counter++;
            }
            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during deleting a users with ids: %s :", usersIdForDelete), e);
        }
    }

    @Override
    public void update(Connection connection, Long id, Long roleId) {
        String sql = "UPDATE users SET role_id=? WHERE id=? and undeletable=false ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, roleId);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during updating a users with id: %s :", id), e);
        }
    }

    @Override
    public void update(Connection connection, String email, String encodePassword) {
        String sql = "UPDATE users SET password=? WHERE email=? and undeletable=false ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, encodePassword);
            preparedStatement.setString(2, email);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during updating password for users with email: %s :", email) + e.getMessage(), e);
        }
    }

    @Override
    public User findUserById(Connection connection, Long id) {
        String sql =
                "SELECT users.id as user_id,lastname,firstname,patronymic,email,password,deleted, roles.id as role_id,roles.name as role_name" +
                        " FROM users" +
                        " JOIN roles ON users.role_id = roles.id   where users.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return getUser(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Can't find user with id: %s :", id), e);
        }
    }

    @Override
    public User save(Connection connection, User user) {
        String email = user.getEmail();
        String sql = "INSERT INTO users (lastname,firstname,patronymic,email,password,role_id) values(?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLastName());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getPatronymic());
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getRole().getId());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during saving a user with email: ", email) + e.getMessage(), e);
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong(1);
        String lastName = resultSet.getString(2);
        String firstName = resultSet.getString(3);
        String patronymic = resultSet.getString(4);
        String email = resultSet.getString(5);
        String password = resultSet.getString(6);
        Boolean isDeleted = resultSet.getBoolean(7);
        Long roleId = resultSet.getLong(8);
        String roleName = resultSet.getString(9);
        Role role = new Role(roleId, roleName);
        User user = new User(lastName, firstName, patronymic, email, password, role, isDeleted);
        user.setId(id);
        return user;
    }

    private String getSubString(int size) {
        String subSql = "(";
        for (int i = 0; i < size; i++) {
            subSql += "?,";
        }
        subSql = subSql.substring(0, subSql.length() - 1) + ")";
        return subSql;
    }
}
