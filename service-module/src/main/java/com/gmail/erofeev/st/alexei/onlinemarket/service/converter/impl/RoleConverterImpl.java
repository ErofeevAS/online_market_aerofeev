package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;


import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.RoleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleConverterImpl implements RoleConverter {
    private final String ROLE_PREFIX = "ROLE_";

    @Override
    public RoleDTO toRoleDTO(Role role) {
        if (role != null) {
            Long roleId = role.getId();
            String roleName = ROLE_PREFIX + role.getName();
            return new RoleDTO(roleId, roleName);
        }
        return null;
    }

    @Override
    public Role fromRoleDTO(RoleDTO role) {
        Long roleId = role.getId();
        String roleName = role.getName().split(ROLE_PREFIX)[1];
        return new Role(roleId, roleName);
    }
}
