package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {
    User findByEmail(String email);

    User findByEmailExcludeSecureApiUser(String email);

    Boolean isUserExist(String email);

    List<User> findUsersSortedByEmail(int offset, int amount, boolean showDeleted);
}