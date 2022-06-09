package com.home.keycode.graphics.text.renderer;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoredEdgesWordRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();

    private static final float DEFAULT_STROKE_WIDTH = 0f;
    private static final double Y_OFFSET = 0.25;
    private static final double X_OFFSET = 0.05;
    private static final List<Color> DEFAULT_COLORS = new ArrayList<>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();

    private final List<Font> fonts;
    private final List<Color> colors;
    private final float strokeWidth;

    public ColoredEdgesWordRenderer(final int textSize) {
        this(DEFAULT_COLORS, DEFAULT_FONTS, DEFAULT_STROKE_WIDTH, textSize);
    }

    public ColoredEdgesWordRenderer(final float strokeWidth, final int textSize) {
        this(DEFAULT_COLORS, DEFAULT_FONTS, strokeWidth, textSize);
    }

    public ColoredEdgesWordRenderer(final List<Color> colors, final List<Font> fonts, final int textSize) {
        this(colors, fonts, DEFAULT_STROKE_WIDTH, textSize);
    }

    public ColoredEdgesWordRenderer(final List<Color> colors, final List<Font> fonts, final float strokeWidth, final int textSize) {
        DEFAULT_COLORS.add(Color.white);

        DEFAULT_FONTS.add(new Font("Geneva", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, textSize));


        this.colors = colors != null ? colors : DEFAULT_COLORS;
        this.fonts = fonts != null ? fonts : DEFAULT_FONTS;
        this.strokeWidth = strokeWidth < 0 ? DEFAULT_STROKE_WIDTH : strokeWidth;
    }

    @Override
    public void render(final String word, final BufferedImage image) {
        final var graphics = image.createGraphics();

        final var hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        graphics.setRenderingHints(hints);

        final var attributedString = new AttributedString(word);
        attributedString.addAttribute(TextAttribute.FONT, getRandomFont());

        final var frc = graphics.getFontRenderContext();
        final var aci = attributedString.getIterator();

        final var textLayout = new TextLayout(aci, frc);
        final var xBaseline = (int) Math.round(image.getWidth() * X_OFFSET);
        final var yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * Y_OFFSET);
        final var shape = textLayout.getOutline(AffineTransform.getTranslateInstance(xBaseline, yBaseline));

        graphics.setColor(getRandomColor());
        graphics.setStroke(new BasicStroke(strokeWidth));

        graphics.draw(shape);
    }

    private Color getRandomColor() {
        return (Color) getRandomObject(colors);
    }

    private Font getRandomFont() {
        return (Font) getRandomObject(fonts);
    }

    private Object getRandomObject(final List<?> objs) {
        if (objs.size() == 1) {
            return objs.get(0);
        }
        return objs.get(RAND.nextInt(objs.size()));
    }
}
