package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {
    User findByEmail(String email);

    Boolean isUserExist(String email);

    List<User> findAllSortedByEmail(int offset, int amount);
}