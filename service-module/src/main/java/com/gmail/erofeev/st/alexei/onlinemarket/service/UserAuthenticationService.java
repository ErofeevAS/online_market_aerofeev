package com.gmail.erofeev.st.alexei.onlinemarket.service;

import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    Long getSecureUserId(Authentication authentication);

    void isUserThSameLikeAuthorizedUser(Long id, Authentication authentication);
}
