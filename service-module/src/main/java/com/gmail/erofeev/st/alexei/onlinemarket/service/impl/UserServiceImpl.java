package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.MailService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${app.generated.password.length}")
    private int STANDARD_PASSWORD_LENGTH;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           PasswordService passwordService,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public void delete(List<Long> userIdsForDelete) {
        for (Long id : userIdsForDelete) {
            userRepository.deleteUserById(id);
        }
    }

    @Override
    @Transactional
    public UserDTO register(UserDTO userDTO) {
        String email = userDTO.getEmail();
        if (!userRepository.isUserExist(email)) {
            User user = userConverter.fromDTO(userDTO);
            String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepository.findRoleByName(user.getRole().getName());
            user.setRole(role);
            Profile profile = new Profile();
            user.setProfile(profile);
            profile.setUser(user);
            userRepository.persist(user);
            String message = String.format("Hello! %s. Your was registered on www.aerofeev-market.com  your password: %s", userDTO.getFullName(), password);
//            mailService.send(email, "new password", message);
            logger.info(String.format("User with email: %s and password: %s was saved", email, password));
            return userConverter.toDTO(user);
        }
        return null;
    }

    @Override
    @Transactional
    public void changePassword(UserDTO userDTO) {
        String email = userDTO.getEmail();
        User user = userRepository.findByEmail(email);
        String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        userRepository.merge(user);
        String message = String.format("Hello! %s. Your password on www.aerofeev-market.com  was changed on %s", userDTO.getFullName(), password);
        logger.debug(message);
        //        mailService.send(email, "new password", message);
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> findAll(int page, int amount) {
        Integer amountOfEntity = userRepository.getAmountOfEntity();
        int maxPages = (Math.round(amountOfEntity / amount) + 1);
        int offset = getOffset(page, maxPages, amount);
        List<User> users = userRepository.getEntities(offset, amount);
        List<UserDTO> userDTOList = userConverter.toListDTO(users);
        PageDTO<UserDTO> pageDTO = new PageDTO<>();
        pageDTO.setAmountOfPages(maxPages);
        pageDTO.setList(userDTOList);
        return pageDTO;
    }

    @Override
    @Transactional
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public ProfileViewDTO getProfileView(Long id) {
        User user = userRepository.findById(id);
        return userConverter.toProfileViewDTO(user);
    }

    @Override
    @Transactional
    public void updateProfile(Long id, ProfileViewDTO profileViewDTO) {
        User user = userRepository.findById(id);
        String address = profileViewDTO.getAddress();
        String firstName = profileViewDTO.getFirstName();
        String phone = profileViewDTO.getPhone();
        String lastName = profileViewDTO.getLastName();
        Profile profile = user.getProfile();
        profile.setAddress(address);
        profile.setPhone(phone);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public void updateRole(Long id, String roleName) {
        User user = userRepository.findById(id);
        if (user.getUndeletable().equals(true)) {
            return;
        }
        Role role = roleRepository.findRoleByName(roleName);
        user.setRole(role);
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public boolean changeOldPassword(Long id, PasswordDTO passwordDTO) {
        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();
        User user = userRepository.findById(id);
        String passwordFromDataBase = user.getPassword();
        if (passwordEncoder.matches(oldPassword, passwordFromDataBase)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.merge(user);
            logger.debug(String.format("password for user with id: %s was changed", id));
            return true;
        }
        return false;
    }

    private int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }
}