package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ProfileRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.MailService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
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
import java.sql.Connection;
import java.sql.SQLException;
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
    private final ProfileRepository profileRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PasswordService passwordService, MailService mailService, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.mailService = mailService;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public void delete(List<Long> usersIdForDelete) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userRepository.delete(connection, usersIdForDelete);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't delete users with ids: %s", usersIdForDelete), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        String email = userDTO.getEmail();
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User userFindByEmail = userRepository.findUserByEmail(connection, email);
                if (userFindByEmail == null) {
                    User user = userConverter.fromDTO(userDTO);
                    String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
                    user.setPassword(passwordEncoder.encode(password));
                    Role role = roleRepository.findRoleByName(user.getRole().getName());
                    user.setRole(role);
                    user = userRepository.save(connection, user);
                    Long id = user.getId();
                    Profile profile = new Profile();
                    profile.setId(id);
                    profileRepository.save(connection, profile);
                    connection.commit();
                    String message = String.format("Hello! %s. Your was registered on www.aerofeev-market.com  your password: %s", userDTO.getFullName(), password);
                    mailService.send(email, "new password", message);
                    logger.info(String.format("User with email: %s and password: %s was saved", email, password));
                    user.setPassword(password);
                    return userConverter.toDTO(user);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't get register user  with email: %s", email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public void changePassword(UserDTO user) {
        String email = user.getEmail();
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
                String encodePassword = passwordEncoder.encode(password);
                userRepository.update(connection, email, encodePassword);
                connection.commit();
                String message = String.format("Hello! %s. Your password on www.aerofeev-market.com  was changed on %s", user.getFullName(), password);
                mailService.send(email, "new password", message);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't change password for user: %s :", email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
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