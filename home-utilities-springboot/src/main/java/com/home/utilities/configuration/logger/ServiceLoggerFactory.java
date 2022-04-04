package com.home.utilities.configuration.logger;

import org.slf4j.Logger;

public final class ServiceLoggerFactory {

    private ServiceLoggerFactory() {
    }

    public static ServiceLogger getServiceLogger(final Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("ServiceLogger must not be null");
        }
        return new ServiceLoggerImpl(logger);
    }
}
