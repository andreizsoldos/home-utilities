package com.home.utilities.controller.exception;

import com.home.utilities.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ControllerAdvice
public class ExceptionController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(final HttpServletRequest request, final Exception ex) {
        final var responseStatus = Optional.ofNullable(AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class))
              .orElseThrow(() -> new NotFoundException("Response status not found"));
        final var statusCode = String.valueOf(responseStatus.value().value());

        LOGGER.error("Request: {} raised exception with message: {}", request.getRequestURL(), ex.getMessage());

        return ExceptionHandlerFactory.addModelObject("error", statusCode, ex);
    }
}
