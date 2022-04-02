package com.home.utilities.configuration.security.filters;

import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.IpInfo;
import com.home.utilities.entities.User;
import com.home.utilities.services.IpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String POST = "POST";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Autowired
    private IpInfoService ipInfoService;

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals(POST)) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        final var email = request.getParameter(USERNAME);
        final var password = request.getParameter(PASSWORD);
        final var optionalUser = bruteForceService.findByEmail(email);
        final var ipAddress = ipInfoService.getIpAddress(request);
        final var optionalIp = bruteForceService.findByIp(ipAddress);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            unlockUserAccount(user);
            if (optionalIp.isPresent()) {
                final var ipInfo = optionalIp.get();
                unlockIp(ipInfo);
            }
        } else {
            if (optionalIp.isPresent()) {
                final var ipInfo = optionalIp.get();
                unlockIp(ipInfo);
            }
        }

        final var authRequest = new UsernamePasswordAuthenticationToken(email, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void unlockUserAccount(final User user) {
        if (user.getFailedLoginAttempts() > 0 && user.getStatus().equals(AccountStatus.LOCKED) && bruteForceService.isUserLockDurationExpired(user)) {
            bruteForceService.unlockUserAccount(user);
        }
    }

    private void unlockIp(final IpInfo ipInfo) {
        if (ipInfo.getFailedIpAttempts() > 0 && bruteForceService.isIpLockDurationExpired(ipInfo)) {
            bruteForceService.unlockIp(ipInfo);
        }
    }
}
