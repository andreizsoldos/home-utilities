package com.home.keycode.filters;

import com.home.keycode.service.KeyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Order(1)
@Component
public class KeyCodeFilter implements Filter {

    private static final List<String> URI = List.of("/login", "/register");
    private static final String GET = "GET";

    @Autowired
    private KeyCodeService keyCodeService;

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;

        if (!isAuthenticated() && URI.contains(request.getRequestURI()) && request.getMethod().equals(GET)) {
            keyCodeService.generateKeyCode();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticated() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
