package com.home.keycode.graphics.text.producer;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;

public class DefaultTextProducer implements TextProducer {

    private static final Random RAND = new SecureRandom();
    private static final long DEFAULT_LENGTH = 5L;
    private static final char[] DEFAULT_CHARS = new char[]{
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'q', 'r', 's', 'u', 'w', 'x', 'y',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'V', 'W', 'Z',
          '2', '3', '4', '5', '6', '7', '8', '9'};

    private final long textLength;
    private final char[] srcChars;

    public DefaultTextProducer() {
        this(DEFAULT_LENGTH, DEFAULT_CHARS);
    }

    public DefaultTextProducer(final long textLength, final char[] srcChars) {
        this.textLength = textLength;
        this.srcChars = srcChars;
    }

    @Override
    public String getText() {
        return RAND.ints(textLength, 0, srcChars.length - 1)
              .boxed()
              .map(i -> String.valueOf(srcChars[i]))
              .collect(Collectors.joining());
    }
}
