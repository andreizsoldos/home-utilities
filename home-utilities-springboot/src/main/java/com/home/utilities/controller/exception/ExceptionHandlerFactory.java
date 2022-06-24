package com.home.utilities.controller.exception;

import com.home.utilities.exception.ExpiredTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public abstract class ExceptionHandlerFactory {

    private static final String LOGIN = "/login";
    private static final String HOMEPAGE = "/";
    private static final String SUPPORT_PAGE = "/support";
    private static final String REDIRECT_TEXT_BUTTON = "redirectTextButton";
    private static final String REDIRECT_TEXT_LOGIN = "error.template.button.login";
    private static final String REDIRECT_TEXT_HOME = "error.template.button.home";
    private static final String REDIRECT_TEXT_SUPPORT = "error.template.button.support";
    private static final String REDIRECT_MESSAGE = "redirectMessage";
    private static final String REDIRECT_MESSAGE_LOGIN = "error.template.redirect.login";
    private static final String REDIRECT_MESSAGE_HOME = "error.template.redirect.home";
    private static final String REDIRECT_MESSAGE_SUPPORT = "error.template.redirect.support";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String REDIRECT_LOCATION = "redirectLocation";
    private static final Integer REDIRECT_DURATION = 15; //seconds

    private ExceptionHandlerFactory() {
    }

    public static ModelAndView addModelObject(final String viewName, final String statusCode, final Exception ex) {
        final var mav = new ModelAndView(Optional.of(viewName).orElse(viewName.equals("") ? "error" : viewName));

        if (statusCode.equals(String.valueOf(HttpStatus.ACCEPTED.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.account.202");
            mav.addObject(REDIRECT_LOCATION, LOGIN);
            mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_LOGIN);
            mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_LOGIN);
        } else if (statusCode.equals(String.valueOf(HttpStatus.BAD_REQUEST.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.400");
            mav.addObject(REDIRECT_LOCATION, HOMEPAGE);
            mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_HOME);
            mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_HOME);
        } else if (statusCode.equals(String.valueOf(HttpStatus.FORBIDDEN.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.403");
            mav.addObject(REDIRECT_LOCATION, HOMEPAGE);
            mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_HOME);
            mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_HOME);
        } else if (statusCode.equals(String.valueOf(HttpStatus.NOT_FOUND.value()))) {
            if (ex instanceof ExpiredTokenException) {
                mav.addObject(ERROR_MESSAGE, "error.template.message.token.404");
                mav.addObject(REDIRECT_LOCATION, SUPPORT_PAGE);
                mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_SUPPORT);
                mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_SUPPORT);
            } else {
                mav.addObject(ERROR_MESSAGE, "error.template.message.404");
                mav.addObject(REDIRECT_LOCATION, HOMEPAGE);
                mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_HOME);
                mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_HOME);
            }
        } else if (statusCode.equals(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))) {
            mav.addObject(ERROR_MESSAGE, "error.template.message.500");
            mav.addObject(REDIRECT_LOCATION, HOMEPAGE);
            mav.addObject(REDIRECT_TEXT_BUTTON, REDIRECT_TEXT_HOME);
            mav.addObject(REDIRECT_MESSAGE, REDIRECT_MESSAGE_HOME);
        }

        mav.addObject("redirectDuration", REDIRECT_DURATION);
        mav.addAllObjects(getStatusMap(statusCode));
        return mav;
    }

    private static Map<String, String> getStatusMap(final String statusCode) {
        final var codeIndex = IntStream.range(0, statusCode.length())
              .mapToObj(i -> "c" + i)
              .collect(toList());

        final var digits = statusCode.chars()
              .map(Character::getNumericValue)
              .mapToObj(d -> "" + d)
              .collect(toList());

        return IntStream.range(0, codeIndex.size())
              .collect(HashMap::new,
                    (m, i) -> m.put(codeIndex.get(i), digits.get(i)),
                    Map::putAll);
    }
}
