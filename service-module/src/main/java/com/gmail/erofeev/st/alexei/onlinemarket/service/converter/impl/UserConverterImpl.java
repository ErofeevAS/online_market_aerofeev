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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        Long id = user.getId();
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        String email = user.getEmail();
        String password = user.getPassword();
        Role role = user.getRole();
        Profile profile = user.getProfile();
        ProfileDTO profileDTO = profileConverter.toDTO(profile);
        RoleDTO roleDTO = roleConverter.toRoleDTO(role);
        Boolean deleted = user.getDeleted();
        UserDTO userDTO = new UserDTO(id, lastName, firstName, patronymic, email, roleDTO, deleted);
        userDTO.setProfile(profileDTO);
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
        ProfileDTO profileDTO = userDTO.getProfile();
        Profile profile = profileConverter.fromDTO(profileDTO);
        User user = new User();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setRole(role);
        user.setDeleted(deleted);
        user.setProfile(profile);
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

    @Override
    public ProfileViewDTO toProfileViewDTO(User user) {
        Long id = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        Profile profile = user.getProfile();
        String address = profile.getAddress();
        String phone = profile.getPhone();
        ProfileViewDTO profileViewDTO = new ProfileViewDTO();
        profileViewDTO.setId(id);
        profileViewDTO.setAddress(address);
        profileViewDTO.setFirstName(firstName);
        profileViewDTO.setLastName(lastName);
        profileViewDTO.setPhone(phone);
        return profileViewDTO;
    }

    @Override
    public User fromProfileViewDTO(ProfileViewDTO profileViewDTO) {
        Long id = profileViewDTO.getId();
        String address = profileViewDTO.getAddress();
        String firstName = profileViewDTO.getFirstName();
        String lastName = profileViewDTO.getLastName();
        String phone = profileViewDTO.getPhone();
        Profile profile = new Profile();
        profile.setAddress(address);
        profile.setPhone(phone);
        User user = new User();
        user.setId(id);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setProfile(profile);
        return user;
    }
}