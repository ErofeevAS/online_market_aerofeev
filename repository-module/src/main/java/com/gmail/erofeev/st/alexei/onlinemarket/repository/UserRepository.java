package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {
    User findByEmail(String email);

    Boolean isUserExist(String email);
}