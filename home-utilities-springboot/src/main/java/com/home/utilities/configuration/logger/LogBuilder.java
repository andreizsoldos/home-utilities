package com.home.utilities.configuration.logger;

import org.apache.commons.lang3.StringUtils;

public class LogBuilder {

    private final String step;
    private final String event;
    private String branch;
    private String clientId;
    private String indexId;
    private String detailMessage;
    private String reason;

    public LogBuilder(final String event, final String step) {
        this.event = event;
        this.step = step;
    }

    public LogBuilder branch(final String branch) {
        this.branch = branch;
        return this;
    }

    public LogBuilder clientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    public LogBuilder indexId(final String indexId) {
        this.indexId = indexId;
        return this;
    }

    public LogBuilder detailMessage(final String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public LogBuilder reason(final String reason) {
        this.reason = reason;
        return this;
    }

    public String buildMessage() {
        final var stringBuilder = new StringBuilder();
        stringBuilder.append(format(LogKey.EVENT_TYPE, keyValidation(this.event)));
        stringBuilder.append(format(LogKey.STEP, keyValidation(this.step)));
        return messageValidation(stringBuilder);
    }

    private String format(final LogKey logKey, final String value) {
        return String.format(logKey.getName().concat("=%s, "), value);
    }

    private String keyValidation(final String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Event and step keys must not be null or empty. Encountered only " + value + ".");
        }
        return value;
    }

    private String messageValidation(final StringBuilder stringBuilder) {
        if (!StringUtils.isEmpty(this.branch)) {
            stringBuilder.append(format(LogKey.BRANCH, this.branch));
        }
        if (!StringUtils.isEmpty(this.clientId)) {
            stringBuilder.append(format(LogKey.CLIENT_ID, this.clientId));
        }
        if (!StringUtils.isEmpty(this.indexId)) {
            stringBuilder.append(format(LogKey.INDEX_ID, this.indexId));
        }
        if (!StringUtils.isEmpty(this.detailMessage)) {
            stringBuilder.append(format(LogKey.DETAIL_MESSAGE, this.detailMessage));
        }
        if (!StringUtils.isEmpty(this.reason)) {
            stringBuilder.append(format(LogKey.REASON, this.reason));
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }
}
