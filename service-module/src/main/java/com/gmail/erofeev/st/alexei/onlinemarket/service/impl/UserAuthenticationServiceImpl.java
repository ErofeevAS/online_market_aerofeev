package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
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
                throw new RuntimeException("GET OUTTA HERE MAN");
            }
        }
    }
}
