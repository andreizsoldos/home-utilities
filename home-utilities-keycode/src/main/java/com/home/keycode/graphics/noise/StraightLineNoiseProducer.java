package com.home.keycode.graphics.noise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

public class StraightLineNoiseProducer implements NoiseProducer {

    private static final SecureRandom RAND = new SecureRandom();

    private final Color color;
    private final int thickness;


    public StraightLineNoiseProducer() {
        this(Color.RED, 4);
    }

    public StraightLineNoiseProducer(final Color color, final int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void makeNoise(final BufferedImage image) {
        final var graphics = image.createGraphics();
        final var height = image.getHeight();
        final var width = image.getWidth();
        final var y1 = RAND.nextInt(height) + 1;
        final var y2 = RAND.nextInt(height) + 1;

        drawLine(graphics, y1, width, y2);
    }

    private void drawLine(final Graphics graphics, final int y1, final int x2, final int y2) {
        final var x1 = 0;

        graphics.setColor(color);
        final var dX = x2 - x1;
        final var dY = y2 - y1;

        final var lineLength = Math.sqrt((double) dX * dX + (double) dY * dY);

        final var scale = thickness / (2 * lineLength);

        double ddx = -scale * dY;
        double ddy = scale * dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        final var dx = (int) ddx;
        final var dy = (int) ddy;

        final int[] xPoints = new int[4];
        final int[] yPoints = new int[4];

        xPoints[0] = x1 + dx;
        yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx;
        yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx;
        yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx;
        yPoints[3] = y2 + dy;

        graphics.fillPolygon(xPoints, yPoints, 4);
    }
}
