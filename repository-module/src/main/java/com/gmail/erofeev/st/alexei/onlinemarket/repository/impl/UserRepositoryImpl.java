package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User findUserByEmail(Connection connection, String email) {
        String sql = "SELECT user.id as user_id,lastname,firstname,patronymic,email,password,deleted, role.id as role_id,role.name as role_name" +
                " FROM user" +
                " JOIN role ON user.role_id = role.id   where user.email=?";
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
        String subSql = getSQLParameters(usersIdForDelete.size());
        String sql = "UPDATE user SET deleted=true WHERE id IN " + subSql + " and undeletable=false ";
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
        String sql = "UPDATE user SET role_id=? WHERE id=? and undeletable=false ";
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
        String sql = "UPDATE user SET password=? WHERE email=? and undeletable=false ";
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
    public User findByEmail(String email) {
        String query = "select u from " + entityClass.getName() + " u where u.email = :email";
        Query q = entityManager.createQuery(query, User.class);
        q.setParameter("email", email);
        return (User) q.getSingleResult();
    }

    @Override
    public User save(Connection connection, User user) {
        String email = user.getEmail();
        String sql = "INSERT INTO user (lastname,firstname,patronymic,email,password,role_id) values(?,?,?,?,?,?)";
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
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setDeleted(isDeleted);
        user.setId(id);
        return user;
    }

    private String getSQLParameters(int size) {
        String subSql = "(";
        for (int i = 0; i < size; i++) {
            subSql += "?,";
        }
        subSql = subSql.substring(0, subSql.length() - 1) + ")";
        return subSql;
    }
}
