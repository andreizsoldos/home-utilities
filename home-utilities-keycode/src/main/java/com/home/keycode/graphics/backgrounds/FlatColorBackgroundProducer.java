package com.home.keycode.graphics.backgrounds;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public final class FlatColorBackgroundProducer implements BackgroundProducer {

    private final Color color;

    public FlatColorBackgroundProducer() {
        this(Color.GRAY);
    }

    public FlatColorBackgroundProducer(final Color color) {
        this.color = color;
    }

    @Override
    public BufferedImage addBackground(final BufferedImage bi) {
        final var width = bi.getWidth();
        final var height = bi.getHeight();

        return this.getBackground(width, height);
    }

    @Override
    public BufferedImage getBackground(final int width, final int height) {
        final var img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final var graphics = img.createGraphics();

        graphics.setPaint(color);
        graphics.fill(new Rectangle2D.Double(0, 0, width, height));
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return img;
    }
}
