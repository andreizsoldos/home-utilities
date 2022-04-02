package com.home.utilities.configuration.security.authentication;

import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.entities.AccountStatus;
import com.home.utilities.entities.IpInfo;
import com.home.utilities.entities.User;
import com.home.utilities.services.IpInfoService;
import com.home.utilities.services.util.Translation;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Locale;

public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String ACCOUNT_LOCKED = "login.locked.account.message";
    private static final String IP_LOCKED = "login.locked.ip.message";
    private static final String BAD_CREDENTIALS = "login.badCredentials.message";
    private static final String LOCK_DURATION = "LOCK_DURATION";
    private static final String USERNAME = "username";
    private static final String DEFAULT_FAILURE_URL = "/login?error";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Autowired
    private IpInfoService ipInfoService;

    @Autowired
    private Translation translation;

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
        final var ipAddress = ipInfoService.getIpAddress(request);
        final var optionalIp = bruteForceService.findByIp(ipAddress);
        final var maxUserFailedAttempts = bruteForceService.getMaximumUserFailedAttempts();
        final var maxIpFailedAttempts = bruteForceService.getMaximumIpFailedAttempts();
        final var locale = localeResolver.resolveLocale(request);

        var errorMessage = translation.getMessage(BAD_CREDENTIALS, locale);
        request.getSession()
              .setAttribute(LOCK_DURATION, null);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (optionalIp.isPresent()) {
                final var ipInfo = optionalIp.get();
                errorMessage = extractMessageIfIpIsPresent(request, maxUserFailedAttempts, maxIpFailedAttempts, locale, errorMessage, user, ipInfo);
            } else {
                errorMessage = extractMessageByAccountStatus(request, maxUserFailedAttempts, locale, errorMessage, user);
            }
        } else {
            if (optionalIp.isPresent()) {
                final var ipInfo = optionalIp.get();
                ipInfo.setEmail(email);
                errorMessage = extractMessageByIpEndLockTime(request, maxIpFailedAttempts, locale, errorMessage, ipInfo);
            } else {
                final var ipInfo = ipInfoService.createIpInfo(request);
                bruteForceService.registerIpFailedLoginAttempts(ipInfo);
                bruteForceService.incrementTotalIpAttempts(ipInfo);
            }
        }

        request.getSession()
              .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }

    private String extractMessageIfIpIsPresent(final HttpServletRequest request, final Integer maxUserFailedAttempts, final Integer maxIpFailedAttempts, final Locale locale, final String errorMessage, final User user, final IpInfo ipInfo) {
        var message = errorMessage;
        final var failedIpCounter = ipInfo.getFailedIpAttempts();
        if (failedIpCounter < maxIpFailedAttempts) {
            message = extractMessageByAccountStatus(request, maxUserFailedAttempts, locale, message, user);
        } else {
            message = translation.getMessage(IP_LOCKED, new Object[]{maxIpFailedAttempts}, locale);
            request.getSession()
                  .setAttribute(LOCK_DURATION, Date.from(ipInfo.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
        }
        return message;
    }

    private String extractMessageByIpEndLockTime(final HttpServletRequest request, final Integer maxIpFailedAttempts, final Locale locale, final String errorMessage, final IpInfo ipInfo) {
        var message = errorMessage;
        bruteForceService.registerIpFailedLoginAttempts(ipInfo);
        bruteForceService.incrementTotalIpAttempts(ipInfo);
        final var failedIpCounter = ipInfo.getFailedIpAttempts();
        if (maxIpFailedAttempts <= failedIpCounter && ipInfo.getEndLockTime() == null) {
            bruteForceService.lockIp(ipInfo);
            message = translation.getMessage(IP_LOCKED, new Object[]{maxIpFailedAttempts}, locale);
            request.getSession()
                  .setAttribute(LOCK_DURATION, Date.from(ipInfo.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
        } else if (maxIpFailedAttempts <= failedIpCounter && ipInfo.getEndLockTime() != null) {
            message = translation.getMessage(IP_LOCKED, new Object[]{maxIpFailedAttempts}, locale);
            request.getSession()
                  .setAttribute(LOCK_DURATION, Date.from(ipInfo.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
        }
        return message;
    }

    private String extractMessageByAccountStatus(final HttpServletRequest request, final Integer maxUserFailedAttempts, final Locale locale, final String errorMessage, final User user) {
        var message = errorMessage;
        if (user.getStatus().equals(AccountStatus.ACTIVE)) {
            bruteForceService.registerUserFailedLoginAttempts(user);
            final var failedUserCounter = user.getFailedLoginAttempts();
            if (maxUserFailedAttempts <= failedUserCounter) {
                bruteForceService.lockUserAccount(user);
                message = translation.getMessage(ACCOUNT_LOCKED, new Object[]{maxUserFailedAttempts}, locale);
                request.getSession()
                      .setAttribute(LOCK_DURATION, Date.from(user.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
            }
        } else if (user.getStatus().equals(AccountStatus.LOCKED)) {
            message = translation.getMessage(ACCOUNT_LOCKED, new Object[]{maxUserFailedAttempts}, locale);
            request.getSession()
                  .setAttribute(LOCK_DURATION, Date.from(user.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
        }
        return message;
    }
}
