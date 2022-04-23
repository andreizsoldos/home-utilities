package com.home.utilities.configuration.security.filters;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RedirectPageFilter extends GenericFilterBean implements AuthenticationApi {

    private static final List<String> URI = List.of("/login", "/register");

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;

        if (isAuthenticated() && URI.contains(request.getRequestURI())) {
            final var encodedRedirectURL = ((HttpServletResponse) servletResponse)
                  .encodeRedirectURL(request.getContextPath() + "/");

            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", encodedRedirectURL);
        }

        filterChain.doFilter(request, response);
    }
}
