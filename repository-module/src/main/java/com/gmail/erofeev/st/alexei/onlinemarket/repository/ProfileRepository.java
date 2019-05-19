package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;

import java.sql.Connection;

public interface ProfileRepository extends GenericRepository<Long, Profile> {
    void save(Connection connection, Profile profile);
}