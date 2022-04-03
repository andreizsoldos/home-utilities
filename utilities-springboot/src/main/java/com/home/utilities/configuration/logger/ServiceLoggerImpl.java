package com.home.utilities.configuration.logger;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class ServiceLoggerImpl implements ServiceLogger {

    private final Logger logger;

    @Override
    public void trace(final LogBuilder logBuilder) {
        if (this.logger.isTraceEnabled()) {
            checkBuilder(logBuilder);
            this.logger.trace(logBuilder.buildMessage());
        }
    }

    @Override
    public void trace(final LogBuilder logBuilder, final Throwable throwable) {
        if (this.logger.isTraceEnabled()) {
            checkBuilder(logBuilder);
            this.logger.trace(logBuilder.buildMessage(), throwable);
        }
    }

    @Override
    public void debug(final LogBuilder logBuilder) {
        if (this.logger.isDebugEnabled()) {
            checkBuilder(logBuilder);
            this.logger.debug(logBuilder.buildMessage());
        }
    }

    @Override
    public void debug(final LogBuilder logBuilder, final Throwable throwable) {
        if (this.logger.isDebugEnabled()) {
            checkBuilder(logBuilder);
            this.logger.debug(logBuilder.buildMessage(), throwable);
        }
    }

    @Override
    public void error(final LogBuilder logBuilder) {
        if (this.logger.isErrorEnabled()) {
            checkBuilder(logBuilder);
            this.logger.error(logBuilder.buildMessage());
        }
    }

    @Override
    public void error(final LogBuilder logBuilder, final Throwable throwable) {
        if (this.logger.isErrorEnabled()) {
            checkBuilder(logBuilder);
            this.logger.error(logBuilder.buildMessage(), throwable);
        }
    }

    @Override
    public void info(final LogBuilder logBuilder) {
        if (this.logger.isInfoEnabled()) {
            checkBuilder(logBuilder);
            this.logger.info(logBuilder.buildMessage());
        }
    }

    @Override
    public void info(final LogBuilder logBuilder, final Throwable throwable) {
        if (this.logger.isInfoEnabled()) {
            checkBuilder(logBuilder);
            this.logger.info(logBuilder.buildMessage(), throwable);
        }
    }

    @Override
    public void warn(final LogBuilder logBuilder) {
        if (this.logger.isWarnEnabled()) {
            checkBuilder(logBuilder);
            this.logger.warn(logBuilder.buildMessage());
        }
    }

    @Override
    public void warn(final LogBuilder logBuilder, final Throwable throwable) {
        if (this.logger.isWarnEnabled()) {
            checkBuilder(logBuilder);
            this.logger.warn(logBuilder.buildMessage(), throwable);
        }
    }

    private static void checkBuilder(final LogBuilder logBuilder) {
        if (logBuilder == null) {
            throw new IllegalArgumentException("ServiceLogger Builder must not be null");
        }
    }
}
