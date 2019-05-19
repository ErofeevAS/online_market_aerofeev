package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;

public interface RoleRepository extends GenericRepository<Long, Role> {
    Role findRoleByName(String roleName);
}