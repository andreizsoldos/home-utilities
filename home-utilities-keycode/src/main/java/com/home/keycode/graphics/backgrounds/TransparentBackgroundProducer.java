package com.home.keycode.graphics.backgrounds;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TransparentBackgroundProducer implements BackgroundProducer {

    @Override
    public BufferedImage addBackground(final BufferedImage image) {
        return this.getBackground(image.getWidth(), image.getHeight());
    }

    @Override
    public BufferedImage getBackground(final int width, final int height) {
        final var img = new BufferedImage(width, height, Transparency.TRANSLUCENT);
        final var graphics = img.createGraphics();

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        graphics.fillRect(0, 0, width, height);

        return img;
    }
}
