package com.home.keycode.graphics.backgrounds;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class SquigglesBackgroundProducer implements BackgroundProducer {

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

        final var bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        graphics.setStroke(bs);

        final var ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
        graphics.setComposite(ac);

        graphics.translate(width * -1.0, 0.0);

        final var delta = 5.0D;
        for (var xt = 0.0; xt < (2.0 * width); xt += delta) {
            final var arc = new Arc2D.Double(0, 0, width, height, 0.0, 360.0, Arc2D.OPEN);
            graphics.draw(arc);
            graphics.translate(delta, 0.0);
        }

        graphics.dispose();

        return img;
    }
}
