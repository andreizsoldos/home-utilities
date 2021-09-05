package com.home.utilities.payload.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IndexRequestDeserializer extends StdDeserializer<Double> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexRequestDeserializer.class);
    private static final String NUMERIC = "^(?=[^a-zA-Z]*[a-zA-Z]).*";

    public IndexRequestDeserializer() {
        this(null);
    }

    protected IndexRequestDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Double deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final var value = p.getText();
        try {
            if (!validate(value, NUMERIC)) {
                return Double.parseDouble(value);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            LOGGER.error("Number format exception: {}", e.getMessage());
            return null;
        }
    }

    private boolean validate(final String value, final String pattern) {
        return value.matches(pattern);
    }
}
