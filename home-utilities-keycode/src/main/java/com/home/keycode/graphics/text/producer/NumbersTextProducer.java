package com.home.keycode.graphics.text.producer;

public class NumbersTextProducer implements TextProducer {

    private static final long DEFAULT_LENGTH = 5L;
    private static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final TextProducer textProducer;

    public NumbersTextProducer() {
        this(DEFAULT_LENGTH);
    }

    public NumbersTextProducer(final long textLength) {
        this.textProducer = new DefaultTextProducer(textLength, NUMBERS);
    }

    @Override
    public String getText() {
        return textProducer.getText();
    }
}
