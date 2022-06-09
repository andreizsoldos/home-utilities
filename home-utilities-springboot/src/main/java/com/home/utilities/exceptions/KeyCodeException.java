package com.home.utilities.exceptions;

import org.springframework.security.core.AuthenticationException;

public class KeyCodeException extends AuthenticationException {

    public KeyCodeException(final String message) {
        super(message);
    }
}
