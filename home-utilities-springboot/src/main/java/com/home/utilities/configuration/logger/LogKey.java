package com.home.utilities.configuration.logger;

public enum LogKey {

    STEP("step"),
    EVENT_TYPE("event"),
    BRANCH("branch"),
    CLIENT_ID("clientId"),
    INDEX_ID("indexId"),
    DETAIL_MESSAGE("detailMessage"),
    REASON("reason");

    private final String name;

    private LogKey(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
