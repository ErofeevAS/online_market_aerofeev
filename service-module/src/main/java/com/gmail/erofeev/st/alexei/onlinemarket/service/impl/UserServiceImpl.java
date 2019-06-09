package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.RoleRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Profile;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Role;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.MailService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.RoleConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserRestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl extends GenericService<UserDTO> implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(GenericService.class);
    @Value("${app.generated.password.length}")
    private int STANDARD_PASSWORD_LENGTH;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter,
                           RoleRepository roleRepository,
                           RoleConverter roleConverter, PasswordEncoder passwordEncoder,
                           PasswordService passwordService,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        logger.debug(String.format("User with email:%s was found", email));
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public void delete(List<Long> userIdsForDelete) {
        for (Long id : userIdsForDelete) {
            User user = userRepository.findById(id);
            if (!user.getUndeletable()) {
                userRepository.remove(user);
                logger.debug(String.format("User with id:%s was deleted", id));
            }
        }
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        if (!userRepository.isUserExist(email)) {
            User user = userConverter.fromDTO(userDTO);
            String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepository.findById(user.getRole().getId());
            user.setRole(role);
            Profile profile = user.getProfile();
            profile.setUser(user);
            userRepository.persist(user);
            String message = String.format("Hello! %s. Your was registered on www.aerofeev-market.com  your password: %s", userDTO.getFullName(), password);
//            mailService.send(email, "new password", message);
            logger.debug(String.format("User with email: %s and password: %s was saved on www.aerofeev-market.com", email, password));
            return userConverter.toDTO(user);
        }
        return null;
    }

    @Override
    @Transactional
    public void changePassword(Long id) {
        User user = userRepository.findById(id);
        String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        userRepository.merge(user);
        String message = String.format("Hello! %s. Your password on www.aerofeev-market.com  was changed on %s", user.getEmail(), password);
        logger.debug(message);
        String email = user.getEmail();
//        mailService.send(email, "new password", message);
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUsers(int page, int amount, boolean showDeleted) {
        Integer amountOfEntity = userRepository.getAmountOfEntity(showDeleted);
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<User> users = userRepository.findUsersSortedByEmail(offset, amount, showDeleted);
        List<UserDTO> userListDTO = userConverter.toListDTO(users);
        return getPageDTO(userListDTO, maxPages);
    }

    @Override
    @Transactional
    public UserDTO findUserByEmailExcludeSecureApiUser(String email) {
        User user = userRepository.findByEmailExcludeSecureApiUser(email);
        logger.debug(String.format("User with email:%s was found", email));
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UserRestDTO registerFromRest(UserRestDTO userDTO) {
        String email = userDTO.getEmail();
        if (!userRepository.isUserExist(email)) {
            User user = userConverter.fromRestDTO(userDTO);
            String password = passwordService.generatePassword(STANDARD_PASSWORD_LENGTH);
            user.setPassword(passwordEncoder.encode(password));
            Long roleId = user.getRole().getId();
            Role role = roleRepository.findById(roleId);
            if (role == null) {
                String message = String.format("role with id:%s not exist", roleId);
                logger.error(message);
                throw new RestEntityNotFoundException(message);
            }
            user.setRole(role);
            Profile profile = user.getProfile();
            profile.setUser(user);
            userRepository.persist(user);
            String message = String.format("Hello! %s. Your was registered on www.aerofeev-market.com  your password: %s", userDTO.getEmail(), password);
//            mailService.send(email, "new password", message);
            logger.debug(String.format("User with email: %s and password: %s was saved on www.aerofeev-market.com", email, password));
            return userConverter.toRestDTO(user);
        }
        String message = String.format("user same email: %s  exist", email);
        logger.error(message);
        throw new RestEntityNotFoundException(message);
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
        updateUserFields(user, profileViewDTO);
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleConverter.toListDTO(roles);
    }

    @Override
    @Transactional
    public void updateRole(Long id, Long roleId) {
        User user = userRepository.findById(id);
        if (user.getUndeletable().equals(true)) {
            return;
        }
        Role role = roleRepository.findById(roleId);
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

    private void updateUserFields(User user, ProfileViewDTO profileViewDTO) {
        String address = profileViewDTO.getAddress();
        String firstName = profileViewDTO.getFirstName();
        String phone = profileViewDTO.getPhone();
        String lastName = profileViewDTO.getLastName();
        Profile profile = user.getProfile();
        profile.setAddress(address);
        profile.setPhone(phone);
        user.setLastName(lastName);
        user.setFirstName(firstName);
    }
}