package com.home.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(final String resourceName) {
        super(String.format("%s has expired", resourceName));
    }
}
