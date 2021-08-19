package com.home.utilities.services.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RequiredArgsConstructor
public class Translation {

    private final MessageSource messageSource;

    public String getMessage(final String message, final Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }
}
