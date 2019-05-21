package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;

import java.util.List;

public interface UserConverter {
    UserDTO toDTO(User user);

    User fromDTO(UserDTO userDTO);

    List<UserDTO> toListDTO(List<User> users);

    ProfileViewDTO toProfileViewDTO(User user);

    User fromProfileViewDTO(ProfileViewDTO profileViewDTO);
}