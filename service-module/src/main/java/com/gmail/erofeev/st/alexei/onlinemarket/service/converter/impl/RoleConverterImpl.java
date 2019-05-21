package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;


import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.RoleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleConverterImpl implements RoleConverter {

    @Override
    public RoleDTO toRoleDTO(Role role) {
        if (role != null) {
            Long roleId = role.getId();
            String roleName = role.getName();
            return new RoleDTO(roleId, roleName);
        }
        return new RoleDTO();
    }

    @Override
    public Role fromRoleDTO(RoleDTO role) {
        Long roleId = role.getId();
        String roleName = role.getName();
        return new Role(roleId, roleName);
    }
}
