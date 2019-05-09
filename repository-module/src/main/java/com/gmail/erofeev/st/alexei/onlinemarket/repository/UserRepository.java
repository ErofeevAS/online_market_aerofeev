package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {
    List<User> getUsers(Connection connection, int offset, int amount);

    User save(Connection connection, User user);

    User findUserByEmail(Connection connection, String email);

    void delete(Connection connection, List<Long> usersIdForDelete);

    void updateRole(Connection connection, Long id, Long roleId);

}
