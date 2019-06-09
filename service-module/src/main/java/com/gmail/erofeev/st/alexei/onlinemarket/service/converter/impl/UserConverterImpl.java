package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ProfileConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.RoleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserRestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverterImpl implements UserConverter {
    private final RoleConverter roleConverter;
    private final ProfileConverter profileConverter;

    public UserConverterImpl(RoleConverter roleConverter, ProfileConverter profileConverter) {
        this.roleConverter = roleConverter;
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setPatronymic(user.getPatronymic());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setDeleted(user.getDeleted());
        Role role = user.getRole();
        RoleDTO roleDTO = roleConverter.toDTO(role);
        userDTO.setRole(roleDTO);
        Profile profile = user.getProfile();
        ProfileDTO profileDTO = profileConverter.toDTO(profile);
        userDTO.setProfile(profileDTO);
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setPatronymic(userDTO.getPatronymic());
        user.setEmail(userDTO.getEmail());
        user.setDeleted(userDTO.getDeleted());
        RoleDTO roleDTO = userDTO.getRole();
        Role role = roleConverter.fromDTO(roleDTO);
        user.setRole(role);
        ProfileDTO profileDTO = new ProfileDTO();
        Profile profile = profileConverter.fromDTO(profileDTO);
        user.setProfile(profile);
        return user;
    }

    @Override
    public User fromRestDTO(UserRestDTO userRestDTO) {
        User user = new User();
        Role role = new Role();
        role.setId(userRestDTO.getRoleId());
        user.setRole(role);
        user.setProfile(new Profile());
        user.setEmail(userRestDTO.getEmail());
        user.setLastName(userRestDTO.getLastName());
        user.setFirstName(userRestDTO.getFirstName());
        return user;
    }

    @Override
    public UserRestDTO toRestDTO(User user) {
        UserRestDTO userDTO = new UserRestDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setId(user.getId());
        userDTO.setRoleId(user.getRole().getId());
        return userDTO;
    }

    @Override
    public List<UserDTO> toListDTO(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProfileViewDTO toProfileViewDTO(User user) {
        ProfileViewDTO profileViewDTO = new ProfileViewDTO();
        profileViewDTO.setId(user.getId());
        profileViewDTO.setFirstName(user.getFirstName());
        profileViewDTO.setLastName(user.getLastName());
        Profile profile = user.getProfile();
        profileViewDTO.setAddress(profile.getAddress());
        profileViewDTO.setPhone(profile.getPhone());
        return profileViewDTO;
    }

    @Override
    public User fromProfileViewDTO(ProfileViewDTO profileViewDTO) {
        Profile profile = new Profile();
        profile.setAddress(profileViewDTO.getAddress());
        profile.setPhone(profileViewDTO.getPhone());
        User user = new User();
        user.setProfile(profile);
        user.setId(profileViewDTO.getId());
        user.setLastName(profileViewDTO.getLastName());
        user.setFirstName(profileViewDTO.getFirstName());
        return user;
    }
}