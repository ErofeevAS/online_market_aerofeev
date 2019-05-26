package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO findUserByEmail(String email);

    void delete(List<Long> usersIdForDelete);

    UserDTO register(UserDTO user);

    void changePassword(Long id);

    PageDTO<UserDTO> findAll(int page, int amount);

    void updateRole(Long id, String roleName);

    boolean changeOldPassword(Long id, PasswordDTO passwordDTO);

    ProfileViewDTO getProfileView(Long id);

    void updateProfile(Long id, ProfileViewDTO profileViewDTO);
}