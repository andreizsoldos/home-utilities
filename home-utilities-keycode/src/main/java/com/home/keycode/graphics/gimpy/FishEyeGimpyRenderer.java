package com.home.keycode.graphics.gimpy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FishEyeGimpyRenderer implements GimpyRenderer {

    private final Color hColor;
    private final Color vColor;

    public FishEyeGimpyRenderer() {
        this(Color.BLACK, Color.BLACK);
    }

    public FishEyeGimpyRenderer(final Color hColor, final Color vColor) {
        this.hColor = hColor;
        this.vColor = vColor;
    }

    @Override
    public void gimp(final BufferedImage image) {
        final var height = image.getHeight();
        final var width = image.getWidth();

        final var hStripes = height / 7;
        final var vStripes = width / 7;

        final var hSpace = height / (hStripes + 1);
        final var vSpace = width / (vStripes + 1);

        final var graph = (Graphics2D) image.getGraphics();
        for (int i = hSpace; i < height; i += hSpace) {
            graph.setColor(hColor);
            graph.drawLine(0, i, width, i);
        }

        for (int i = vSpace; i < width; i += vSpace) {
            graph.setColor(vColor);
            graph.drawLine(i, 0, i, height);
        }

        final int[] pix = new int[height * width];
        int j = 0;
        for (int j1 = 0; j1 < width; j1++) {
            for (int k1 = 0; k1 < height; k1++) {
                pix[j] = image.getRGB(j1, k1);
                j++;
            }
        }

        final var distance = ranInt(width / 4, width / 3);

        final var wMid = image.getWidth() / 2;
        final var hMid = image.getHeight() / 2;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final var relX = x - wMid;
                final var relY = y - hMid;

                final var d1 = Math.sqrt((double) relX * relX + (double) relY * relY);
                if (d1 < distance) {
                    final var j2 = wMid + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (x - wMid));
                    final var k2 = hMid + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (y - hMid));
                    image.setRGB(x, y, pix[j2 * height + k2]);
                }
            }
        }

        graph.dispose();
    }

    private int ranInt(final int i, final int j) {
        final var d = Math.random();
        return (int) (i + ((j - i) + 1) * d);
    }

    private double fishEyeFormula(final double s) {
        if (s < 0.0D) {
            return 0.0D;
        }
        if (s > 1.0D) {
            return s;
        }
        return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
}
