package com.home.utilities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailException extends RuntimeException {

    public EmailException(final String message, final Object fieldValue) {
        super(String.format("%s %s", message, fieldValue));
    }
}
