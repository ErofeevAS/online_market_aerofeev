package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers(int page, int amount);

    Integer getAmount(int amountOfDisplayedUsers);

    UserDTO findUserByEmail(String email);

    void delete(List<Long> usersIdForDelete);

    void updateRole(Long id, String roleName);

    UserDTO register(UserDTO user);

    void changePassword(UserDTO user);

    UserDTO findUserById(Long id);
}
