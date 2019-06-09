package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;

import java.util.List;

public interface RoleConverter {
    RoleDTO toDTO(Role role);

    Role fromDTO(RoleDTO role);

    List<RoleDTO> toListDTO(List<Role> roles);
}