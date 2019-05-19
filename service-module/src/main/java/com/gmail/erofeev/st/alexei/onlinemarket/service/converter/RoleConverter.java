package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;

public interface RoleConverter {
    RoleDTO toRoleDTO(Role role);

    Role fromRoleDTO(RoleDTO role);
}