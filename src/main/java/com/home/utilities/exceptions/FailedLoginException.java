package com.home.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FailedLoginException extends AuthenticationException {

    public FailedLoginException(final String message) {
        super(message);
    }
}
