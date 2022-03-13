package com.home.utilities.configuration.security.authentication;

import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.entities.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String LOCKED = "login.locked.message";
    private static final String BAD_CREDENTIALS = "login.badCredentials.message";
    private static final String LOCK_DURATION = "LOCK_DURATION";
    private static final String USERNAME = "username";
    private static final String DEFAULT_FAILURE_URL = "/login?error";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {

        super.setDefaultFailureUrl(DEFAULT_FAILURE_URL);
        super.onAuthenticationFailure(request, response, exception);

        final var email = request.getParameter(USERNAME);
        final var optionalUser = bruteForceService.findByEmail(email);
        final var locale = localeResolver.resolveLocale(request);
        final var maxFailedAttempts = bruteForceService.getMaximumFailedAttempts();

        var errorMessage = messageSource.getMessage(BAD_CREDENTIALS, null, locale);
        request.getSession().setAttribute(LOCK_DURATION, null);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (user.getStatus().equals(AccountStatus.ACTIVE)) {
                bruteForceService.registerFailedLoginAttempts(user);
                final var failedCounter = user.getFailedLoginAttempts();
                if (maxFailedAttempts <= failedCounter) {
                    bruteForceService.lockAccount(user);
                    errorMessage = messageSource.getMessage(LOCKED, new Object[]{maxFailedAttempts}, locale);
                    request.getSession().setAttribute(LOCK_DURATION, Date.from(user.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
                }
            } else if (user.getStatus().equals(AccountStatus.LOCKED)) {
                errorMessage = messageSource.getMessage(LOCKED, new Object[]{maxFailedAttempts}, locale);
                request.getSession().setAttribute(LOCK_DURATION, Date.from(user.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
            }
        } else {
            // TODO - implement IP failure login here
        }

        request.getSession()
              .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}
