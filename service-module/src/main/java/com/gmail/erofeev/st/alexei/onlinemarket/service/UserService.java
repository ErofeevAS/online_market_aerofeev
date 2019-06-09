package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserRestDTO;

import java.util.List;

public interface UserService {

    UserDTO findUserByEmail(String email);

    void delete(List<Long> usersIdForDelete);

    UserDTO registerUser(UserDTO user);

    void changePassword(Long id);

    void updateRole(Long id, Long roleId);

    boolean changeOldPassword(Long id, PasswordDTO passwordDTO);

    ProfileViewDTO getProfileView(Long id);

    void updateProfile(Long id, ProfileViewDTO profileViewDTO);

    List<RoleDTO> getAllRoles();

    PageDTO<UserDTO> getUsers(int page, int size, boolean showDeleted);

    UserDTO findUserByEmailExcludeSecureApiUser(String email);

    UserRestDTO registerFromRest(UserRestDTO userDTO);
}