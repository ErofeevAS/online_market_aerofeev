package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String STANDARD_PASSWORD = "1234";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userConverter.fromDTO(userDTO);
                Role role = roleRepository.findRoleByName(connection, user.getRole().getName());
                user.setPassword(passwordEncoder.encode(STANDARD_PASSWORD));
                user.setRole(role);
                user = userRepository.save(connection, user);
                connection.commit();
                return userConverter.toDTO(user);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public Integer getAmount(int amountOfDisplayedUsers) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer amount = userRepository.getAmount(connection,"users");
                amount = (Math.round(amount / amountOfDisplayedUsers) + 1);
                connection.commit();
                return amount;
            } catch (SQLException e) {
                connection.rollback();
                String message = "Can't get amount of users from repository: " + e.getMessage();
                logger.error(message, e);
                throw new ServiceException(message, e);
            }
        } catch (SQLException e) {
            String message = "Can't establish connection to database: " + e.getMessage();
            logger.error(message, e);
            throw new ServiceException(message, e);
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
                logger.error("Database error, changes were rollbacked: " + e.getMessage(), e);
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Can't establish connection to database:" + e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
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
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateRole(Long id, String roleName) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                roleName = roleName.split("_")[1];
                Role role = roleRepository.findRoleByName(connection, roleName);
                userRepository.updateRole(connection, id, role.getId());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
