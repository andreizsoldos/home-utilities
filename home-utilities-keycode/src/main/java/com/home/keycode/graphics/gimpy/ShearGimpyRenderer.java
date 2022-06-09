package com.home.keycode.graphics.gimpy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

public class ShearGimpyRenderer implements GimpyRenderer {

    private static final Random RAND = new SecureRandom();

    private final Color color;

    public ShearGimpyRenderer() {
        this(Color.GRAY);
    }

    public ShearGimpyRenderer(final Color color) {
        this.color = color;
    }

    @Override
    public void gimp(final BufferedImage image) {
        final var graphics = image.createGraphics();

        shearX(graphics, image.getWidth(), image.getHeight());
        shearY(graphics, image.getWidth(), image.getHeight());

        graphics.dispose();
    }

    private void shearX(final Graphics2D graphics, final int w1, final int h1) {
        final var period = RAND.nextInt(10) + 5;
        final var frames = 15;
        final var phase = RAND.nextInt(5) + 2;

        for (int i = 0; i < h1; i++) {
            final double d = (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * phase) / frames);
            graphics.copyArea(0, i, w1, 1, (int) d, 0);
            graphics.setColor(color);
            graphics.drawLine((int) d, i, 0, i);
            graphics.drawLine((int) d + w1, i, w1, i);
        }
    }

    private void shearY(final Graphics2D graphics, final int w1, final int h1) {
        final var period = RAND.nextInt(30) + 10;
        final var frames = 15;
        final var phase = 7;

        for (int i = 0; i < w1; i++) {
            final double d = (period >> 1) * Math.sin((float) i / period + (6.2831853071795862D * phase) / frames);
            graphics.copyArea(i, 0, 1, h1, 0, (int) d);
            graphics.setColor(color);
            graphics.drawLine(i, (int) d, i, 0);
            graphics.drawLine(i, (int) d + h1, i, h1);
        }
    }
}
