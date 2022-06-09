package com.home.keycode.graphics.text.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultWordRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();
    private static final List<Color> DEFAULT_COLORS = new ArrayList<>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();
    private static final double Y_OFFSET = 0.25;
    private static final double X_OFFSET = 0.05;

    private final List<Color> colors = new ArrayList<>();
    private final List<Font> fonts = new ArrayList<>();

    public DefaultWordRenderer(final int textSize) {
        this(DEFAULT_COLORS, DEFAULT_FONTS, textSize);
    }

    public DefaultWordRenderer(final List<Color> colors, final List<Font> fonts, final int textSize) {
        DEFAULT_COLORS.add(Color.WHITE);

        DEFAULT_FONTS.add(new Font("Geneva", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, textSize));

        this.colors.addAll(colors);
        this.fonts.addAll(fonts);
    }

    @Override
    public void render(final String word, final BufferedImage image) {
        final var graphics = (Graphics2D) image.getGraphics();

        final var hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        graphics.setRenderingHints(hints);

        final var frc = graphics.getFontRenderContext();

        var xBaseline = (int) Math.round(image.getWidth() * X_OFFSET);
        var yBaseline = image.getHeight() - (int) Math.round(image.getHeight() * Y_OFFSET);

        final char[] chars = new char[1];
        for (char c : word.toCharArray()) {
            chars[0] = c;

            graphics.setColor(getRandomColor());

            final var font = getRandomFont();
            graphics.setFont(font);

            final var glyphVector = font.createGlyphVector(frc, chars);
            graphics.drawChars(chars, 0, chars.length, xBaseline, yBaseline);

            final var width = (int) glyphVector.getVisualBounds().getWidth();
            xBaseline = xBaseline + width;
        }
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
