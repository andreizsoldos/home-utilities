package com.home.utilities.configuration.security.filters;

import com.home.keycode.service.KeyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Order(1)
@Component
public class KeyCodeFilter implements Filter, AuthenticationApi {

    private static final List<String> URI = List.of("/login", "/register");
    private static final String SPACE = " ";
    private static final String ENCODED_SPACE = "%20";
    private static final String GENERATED_KEYCODE_FILE_NAME = "keycode.jpg";
    private static final String KEYCODE_OUTPUT_PATH = "static/images/keycode/";

    @Autowired
    private KeyCodeService keyCodeService;

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;

        if (!isAuthenticated() && URI.contains(request.getRequestURI())) {
            keyCodeService.generateKeyCode(saveOutputFile());
        }

        filterChain.doFilter(request, response);
    }

    private File saveOutputFile() {
        final var path = Objects.requireNonNull(this.getClass().getResource("/".concat(KEYCODE_OUTPUT_PATH))).getPath();
        return new File(sanitizePath(path), GENERATED_KEYCODE_FILE_NAME);
    }

    private String sanitizePath(final String path) {
        if (path.startsWith("/")) {
            return path.substring(1).replace(ENCODED_SPACE, SPACE);
        }
        return path.replace(ENCODED_SPACE, SPACE);
    }
}
