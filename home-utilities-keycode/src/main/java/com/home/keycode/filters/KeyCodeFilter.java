package com.home.keycode.filters;

import com.home.keycode.KeyCode;
import com.home.keycode.graphics.backgrounds.SquigglesBackgroundProducer;
import com.home.keycode.graphics.text.producer.AlphaNumericTextProducer;
import com.home.keycode.graphics.text.renderer.ThreeWordsRenderer;
import com.home.keycode.graphics.text.util.FilterUtilProperties;
import com.home.keycode.graphics.text.util.ImageUtilProperties;
import com.home.keycode.graphics.text.util.SecurityTextUtilProperties;
import com.home.keycode.graphics.text.util.TextUtilProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.home.keycode.KeyCode.NAME;

public class KeyCodeFilter extends OncePerRequestFilter {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String SPACE = " ";
    private static final String NO_SPACE = "";
    private static final String COMMA = ",";
    private static final String KEYCODE = "keycode";
    private static final String INVALID_KEYCODE = "login.invalidKeycode.message";

    @Autowired
    private FilterUtilProperties filterUtil;

    @Autowired
    private ImageUtilProperties imageUtil;

    @Autowired
    private TextUtilProperties textUtil;

    @Autowired
    private SecurityTextUtilProperties securityTextUtil;

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return excludeUriFilter(request);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final var submittedKeyCode = request.getParameter(KEYCODE);
        final var uriList = List.of(filterUtil.getUri().replace(SPACE, NO_SPACE).split(COMMA));

        if (isNotAuthenticated() && uriList.contains(request.getRequestURI()) && request.getMethod().equalsIgnoreCase(GET)) {
            generateKeyCode(request, response);
        } else if (isNotAuthenticated() && uriList.contains(request.getRequestURI()) && request.getMethod().equalsIgnoreCase(POST)) {
            validateKeyCode(request, submittedKeyCode);
        }

        filterChain.doFilter(request, response);
    }

    private boolean excludeUriFilter(final HttpServletRequest request) {
        final var path = request.getRequestURI();
        final var excludedUri = filterUtil.getExcludedUri();
        if (StringUtils.hasLength(excludedUri)) {
            return Arrays.stream(excludedUri.replace(SPACE, NO_SPACE).split(COMMA))
                  .anyMatch(path::matches);
        } else {
            return false;
        }
    }

    private void generateKeyCode(final HttpServletRequest request, final HttpServletResponse response) {
        final var keyCode = new KeyCode.Builder(imageUtil.getWidth(), imageUtil.getHeight(), textUtil.getSize())
              .addText(new AlphaNumericTextProducer(textUtil.getLength()), new ThreeWordsRenderer(textUtil.getSize(), securityTextUtil.getLength()))
              .addBackground(new SquigglesBackgroundProducer())
              .build();
        request.getSession().setAttribute(NAME, keyCode);
        response.setHeader("Cache-Control", "private,no-cache,no-store");
    }

    private void validateKeyCode(final HttpServletRequest request, final String submittedKeyCode) {
        final var keyCode = (KeyCode) request.getSession().getAttribute(NAME);
        if (!keyCode.validate(submittedKeyCode)) {
            request.getSession()
                  .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, INVALID_KEYCODE);
        }
    }

    private boolean isNotAuthenticated() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return true;
        }
        return !authentication.isAuthenticated();
    }
}
