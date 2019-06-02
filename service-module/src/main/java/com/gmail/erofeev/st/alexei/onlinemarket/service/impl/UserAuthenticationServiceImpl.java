package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private static final String ACCESS_DENIED_MESSAGE = "you can't get access to other people data";
    @Override
    public Long getSecureUserId(Authentication authentication) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        return principal.getUser().getId();
    }

    @Override
    public void isUserThSameLikeAuthorizedUser(Long userId, Authentication authentication) {
        if (authentication != null) {
            Long secureUserId = getSecureUserId(authentication);
            if (!secureUserId.equals(userId)) {
                throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
            }
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }
}
