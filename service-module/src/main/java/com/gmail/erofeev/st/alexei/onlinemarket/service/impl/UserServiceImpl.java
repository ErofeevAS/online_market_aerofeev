package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.MailService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PasswordService passwordService, MailService mailService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.mailService = mailService;
    }

    @Override
    public List<UserDTO> getUsers(int page, int amount) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int offset = (page - 1) * amount;
                List<User> users = userRepository.getUsers(connection, offset, amount);
                List<UserDTO> usersDTO = userConverter.toListDTO(users);
                connection.commit();
                return usersDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException("Can't get users from repository.", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public Integer getAmount(int amountOfDisplayedUsers) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                amountOfDisplayedUsers = Math.abs(amountOfDisplayedUsers);
                Integer amount = userRepository.getAmount(connection, "users");
                amount = (Math.round(amount / amountOfDisplayedUsers) + 1);
                connection.commit();
                return amount;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException("Can't get amount of users from repository.", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.findUserByEmail(connection, email);
                UserDTO userDTO = userConverter.toDTO(user);
                connection.commit();
                logger.info("user was found: " + user);
                return userDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't get user with email: %s", email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }

    @Override
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
    public void updateRole(Long id, String roleName) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                roleName = roleName.split("_")[1];
                Role role = roleRepository.findRoleByName(connection, roleName);
                userRepository.update(connection, id, role.getId());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't get update role: %s for  user  with id: %s", roleName, id), e);
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
                    Role role = roleRepository.findRoleByName(connection, user.getRole().getName());
                    user.setRole(role);
                    user = userRepository.save(connection, user);
                    connection.commit();
                    String message = String.format("Hello! %s. Your was registered on www.aerofeev-market.com  your password: %s", userDTO.getFullName(), password);
                    mailService.send(email, "new password", message);
                    logger.info(String.format("User with email: %s and password: %s was saved", email, password));
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
    public UserDTO findUserById(Long id) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.findUserById(connection, id);
                UserDTO userDTO = userConverter.toDTO(user);
                connection.commit();
                logger.debug(String.format("user with id:%s was found", id));
                return userDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("Can't get user with id: %s", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("Can't establish connection to database.", e);
        }
    }
}
