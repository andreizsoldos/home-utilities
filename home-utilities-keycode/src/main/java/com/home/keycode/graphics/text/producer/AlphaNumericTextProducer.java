package com.home.keycode.graphics.text.producer;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AlphaNumericTextProducer implements TextProducer {

    private static final Random RAND = new SecureRandom();
    private static final long DEFAULT_LENGTH = 5L;

    private final long textLength;

    public AlphaNumericTextProducer() {
        this(DEFAULT_LENGTH);
    }

    public AlphaNumericTextProducer(final long textLength) {
        this.textLength = textLength;
    }

    @Override
    public String getText() {
        final var asciiNumbersRange = new int[]{48, 57};    // numbers
        final var asciiCapitalLettersRange = new int[]{65, 90};   // capital letters
        final var asciiSmallLettersRange = new int[]{97, 122};   // small letters

        final var numbers = IntStream.rangeClosed(asciiNumbersRange[0], asciiNumbersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(numbers);

        final var capitalLetters = IntStream.rangeClosed(asciiCapitalLettersRange[0], asciiCapitalLettersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(capitalLetters);
        numbers.addAll(capitalLetters);
        Collections.shuffle(numbers);

        final var smallLetters = IntStream.rangeClosed(asciiSmallLettersRange[0], asciiSmallLettersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(smallLetters);
        numbers.addAll(smallLetters);
        Collections.shuffle(numbers);

        return RAND.ints(textLength, 0, numbers.size() - 1)
              .boxed()
              .map(numbers::get)
              .collect(Collectors.joining());
    }
}
