package com.home.utilities.configuration.security.filters;

import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.entities.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        final var email = request.getParameter(USERNAME);
        final var password = request.getParameter(PASSWORD);
        final var optionalUser = bruteForceService.findByEmail(email);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (user.getFailedLoginAttempts() > 0 && user.getStatus().equals(AccountStatus.LOCKED) && bruteForceService.isLockDurationExpired(user)) {
                bruteForceService.unlockAccount(user);
            }
        }

        final var authRequest = new UsernamePasswordAuthenticationToken(email, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
