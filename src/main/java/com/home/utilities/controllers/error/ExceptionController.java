package com.home.utilities.controllers.error;

import com.home.utilities.exceptions.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@ControllerAdvice
public class ExceptionController implements ErrorController {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleError(final Exception ex) {
        final var responseStatus = Optional.ofNullable(AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class))
              .orElseThrow(() -> new NotFoundException("Response status not found"));
        final var statusCode = String.valueOf(responseStatus.value().value());

        return ExceptionHandlerFactory.addModelObject("error", statusCode, ex);
    }
}
