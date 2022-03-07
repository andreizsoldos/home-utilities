package com.home.utilities.configuration.security.authentication;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {

        final var email = ((UserPrincipal) authentication.getPrincipal()).getEmail();
        final var optionalUser = bruteForceService.findByEmail(email);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (user.getFailedLoginAttempts() > 0) {
                bruteForceService.resetFailedLoginAttempts(user);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
