package com.home.keycode.graphics.noise;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

public class CurvedLineNoiseProducer implements NoiseProducer {

    private static final Random RAND = new SecureRandom();

    private final Color color;
    private final float width;

    public CurvedLineNoiseProducer() {
        this(Color.BLACK, 3.0f);
    }

    public CurvedLineNoiseProducer(final Color color, final float width) {
        this.color = color;
        this.width = width;
    }

    @Override
    public void makeNoise(final BufferedImage image) {
        final var imgWidth = image.getWidth();
        final var imgHeight = image.getHeight();

        final var cubicCurve = new CubicCurve2D.Float(imgWidth * .1f, imgHeight
              * RAND.nextFloat(), imgWidth * .1f, imgHeight
              * RAND.nextFloat(), imgWidth * .25f, imgHeight
              * RAND.nextFloat(), imgWidth * .9f, imgHeight
              * RAND.nextFloat());

        final var pathIterator = cubicCurve.getPathIterator(null, 2);
        final Point2D[] tmp = new Point2D[200];

        int i = 0;
        while (!pathIterator.isDone()) {
            final float[] coords = new float[6];
            if (pathIterator.currentSegment(coords) == PathIterator.SEG_MOVETO ||
                  pathIterator.currentSegment(coords) == PathIterator.SEG_LINETO) {
                tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pathIterator.next();
        }

        final Point2D[] pts = new Point2D[i];
        System.arraycopy(tmp, 0, pts, 0, i);

        final var graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        graphics.setColor(color);

        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3) {
                graphics.setStroke(new BasicStroke(width));
            }
            graphics.drawLine((int) pts[i].getX(), (int) pts[i].getY(), (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
        }

        graphics.dispose();
    }
}
