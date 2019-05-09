package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;

import java.sql.Connection;

public interface RoleRepository {
    Role findRoleByName(Connection connection, String roleName);
}
