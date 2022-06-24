package com.home.utilities.exception;

import org.springframework.security.core.AuthenticationException;

public class KeyCodeException extends AuthenticationException {

    public KeyCodeException(final String message) {
        super(message);
    }
}
