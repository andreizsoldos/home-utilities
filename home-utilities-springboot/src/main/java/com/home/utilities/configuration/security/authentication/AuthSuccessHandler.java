package com.home.utilities.configuration.security.authentication;

import com.home.utilities.configuration.userdetails.UserPrincipal;
import com.home.utilities.configuration.userdetails.service.BruteForceProtectionService;
import com.home.utilities.service.IpInfoService;
import com.home.utilities.service.util.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String IP_LOCKED = "login.locked.ip.message";
    private static final String LOCK_DURATION = "LOCK_DURATION";

    @Autowired
    private BruteForceProtectionService bruteForceService;

    @Autowired
    private IpInfoService ipInfoService;

    @Autowired
    private Translation translation;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {

        final var email = ((UserPrincipal) authentication.getPrincipal()).getEmail();
        final var optionalUser = bruteForceService.findByEmail(email);
        final var ipAddress = ipInfoService.getIpAddress(request);
        final var optionalIp = bruteForceService.findByIp(ipAddress);
        final var maxIpFailedAttempts = bruteForceService.getMaximumIpFailedAttempts();
        final var locale = localeResolver.resolveLocale(request);

        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            if (user.getFailedLoginAttempts() > 0) {
                bruteForceService.resetUserFailedLoginAttempts(user);
            }
            if (optionalIp.isPresent()) {
                final var ipInfo = optionalIp.get();
                if (bruteForceService.isIpLockDurationExpired(ipInfo)) {
                    ipInfo.setEmail(email);
                    bruteForceService.unlockIp(ipInfo);
                    bruteForceService.resetIpFailedLoginAttempts(ipInfo);
                } else {
                    final var errorMessage = translation.getMessage(IP_LOCKED, new Object[]{maxIpFailedAttempts}, locale);
                    request.getSession()
                          .setAttribute(LOCK_DURATION, Date.from(ipInfo.getEndLockTime().atZone(ZoneId.systemDefault()).toInstant()).getTime());
                    request.getSession()
                          .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
                    throw new LockedException(errorMessage);
                }
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
