package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;


import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.RoleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserConverterImpl implements UserConverter {
    @Autowired
    private RoleConverter roleConverter;

    @Override
    public UserDTO toDTO(User user) {
        Long id = user.getId();
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        String email = user.getEmail();
        String password = user.getPassword();
        Role role = user.getRole();
        RoleDTO roleDTO = roleConverter.toRoleDTO(role);
        Boolean deleted = user.getDeleted();
        UserDTO userDTO = new UserDTO(id, lastName, firstName, patronymic, email, roleDTO, deleted);
        userDTO.setPassword(password);
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        String lastName = userDTO.getLastName();
        String firstName = userDTO.getFirstName();
        String patronymic = userDTO.getPatronymic();
        String email = userDTO.getEmail();
        RoleDTO roleDTO = userDTO.getRole();
        Role role = roleConverter.fromRoleDTO(roleDTO);
        Boolean deleted = userDTO.getDeleted();
        User user = new User(lastName, firstName, patronymic, email, role, deleted);
        return user;
    }

    @Override
    public List<UserDTO> toListDTO(List<User> users) {
        List<UserDTO> userDTOList = new ArrayList<>(users.size());
        for (User user : users) {
            userDTOList.add(toDTO(user));
        }
        return userDTOList;
    }
}
