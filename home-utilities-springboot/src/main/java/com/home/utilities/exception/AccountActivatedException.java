package com.home.utilities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class AccountActivatedException extends RuntimeException {

    public AccountActivatedException(final String message) {
        super(message);
    }
}
