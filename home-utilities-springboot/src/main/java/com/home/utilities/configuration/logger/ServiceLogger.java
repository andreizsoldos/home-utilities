package com.home.utilities.configuration.logger;

public interface ServiceLogger {

    void trace(LogBuilder logBuilder);

    void trace(LogBuilder logBuilder, Throwable throwable);

    void debug(LogBuilder logBuilder);

    void debug(LogBuilder logBuilder, Throwable throwable);

    void error(LogBuilder logBuilder);

    void error(LogBuilder logBuilder, Throwable throwable);

    void info(LogBuilder logBuilder);

    void info(LogBuilder logBuilder, Throwable throwable);

    void warn(LogBuilder logBuilder);

    void warn(LogBuilder logBuilder, Throwable throwable);
}
