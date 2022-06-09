package com.home.keycode.graphics.gimpy;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class StretchGimpyRenderer implements GimpyRenderer {

    private static final double X_DEFAULT = 1.0;
    private static final double Y_DEFAULT = 3.0;

    private final double xScale;
    private final double yScale;

    public StretchGimpyRenderer() {
        this(X_DEFAULT, Y_DEFAULT);
    }

    public StretchGimpyRenderer(final double xScale, final double yScale) {
        this.xScale = xScale;
        this.yScale = yScale;
    }

    @Override
    public void gimp(final BufferedImage image) {
        final var graphics = image.createGraphics();
        final var affineTransform = new AffineTransform();
        affineTransform.scale(xScale, yScale);
//		RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.drawRenderedImage(image, affineTransform);
    }
}
