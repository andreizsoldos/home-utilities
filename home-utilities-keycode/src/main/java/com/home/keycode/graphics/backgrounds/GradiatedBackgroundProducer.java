package com.home.keycode.graphics.backgrounds;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GradiatedBackgroundProducer implements BackgroundProducer {

    private final Color fromColor;
    private final Color toColor;

    public GradiatedBackgroundProducer() {
        this(Color.DARK_GRAY, Color.WHITE);
    }

    public GradiatedBackgroundProducer(final Color fromColor, final Color toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    @Override
    public BufferedImage addBackground(final BufferedImage image) {
        final var width = image.getWidth();
        final var height = image.getHeight();

        return this.getBackground(width, height);
    }

    @Override
    public BufferedImage getBackground(final int width, final int height) {
        final var img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final var graphics = img.createGraphics();
        final var hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHints(hints);

        final var gradientPaint = new GradientPaint(0, 0, fromColor, width, height, toColor);
        graphics.setPaint(gradientPaint);
        graphics.fill(new Rectangle2D.Double(0, 0, width, height));
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return img;
    }
}
