package com.home.utilities.configuration.security.authentication;

import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.entities.AccountStatus;
import com.home.utilities.exceptions.FailedLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String LOCKED = "AbstractUserDetailsAuthenticationProvider.locked";
    private static final String USERNAME = "username";
    private static final String DEFAULT_FAILURE_URL = "/login?error";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        final var email = request.getParameter(USERNAME);
        final var optionalUser = bruteForceService.findByEmail(email);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (user.getStatus().equals(AccountStatus.ACTIVE)) {
                bruteForceService.registerFailedLoginAttempts(user);
                final var maxFailedAttempts = bruteForceService.getMaximumFailedAttempts();
                final var failedCounter = user.getFailedLoginAttempts();
                if (maxFailedAttempts <= failedCounter) {
                    bruteForceService.lockAccount(user);
                    exception = new FailedLoginException(messageSource.getMessage(
                          LOCKED,
                          null,
                          request.getLocale()));
                }
            }
        } else {
            // TODO - implement IP failure login here
        }

        super.setDefaultFailureUrl(DEFAULT_FAILURE_URL);
        super.onAuthenticationFailure(request, response, exception);
    }
}
