package com.home.keycode.graphics.text.renderer;

import com.home.keycode.graphics.text.producer.AlphaNumericTextProducer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreeWordsRenderer implements WordRenderer {

    private static final Random RAND = new SecureRandom();
    private static final List<Color> DEFAULT_COLORS = new ArrayList<>();
    private static final List<Font> DEFAULT_FONTS = new ArrayList<>();
    private static final int PIXEL_SIZE = 1;

    private final List<Color> colors = new ArrayList<>();
    private final List<Font> fonts = new ArrayList<>();
    private final long securityTextLength;

    public ThreeWordsRenderer(final int textSize, final long securityTextLength) {
        this(DEFAULT_COLORS, DEFAULT_FONTS, textSize, securityTextLength);
    }

    public ThreeWordsRenderer(final List<Color> colors, final List<Font> fonts, final int textSize, final long securityTextLength) {
        DEFAULT_COLORS.add(Color.WHITE);

        DEFAULT_FONTS.add(new Font("Geneva", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Courier", Font.BOLD, textSize));
        DEFAULT_FONTS.add(new Font("Arial", Font.BOLD, textSize));

        this.colors.addAll(colors);
        this.fonts.addAll(fonts);
        this.securityTextLength = securityTextLength;
    }

    @Override
    public void render(final String word, final BufferedImage image) {
        final var graphics = (Graphics2D) image.getGraphics();

        final var hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        graphics.setRenderingHints(hints);
        graphics.setColor(getRandomColor());

        final var securityKeyCode = new AlphaNumericTextProducer(securityTextLength);
        final var demoKeyCodeTopString = securityKeyCode.getText();
        final var demoKeyCodeBottomString = securityKeyCode.getText();
        final var font = getRandomFont();
        graphics.setFont(font);

        final var metrics = graphics.getFontMetrics(font);

        int xBaseline;
        int yBaseLine;
        xBaseline = (image.getWidth() - metrics.stringWidth(demoKeyCodeTopString)) / 2;
        yBaseLine = metrics.getAscent();
        graphics.drawString(demoKeyCodeTopString, xBaseline, yBaseLine);

        xBaseline = (image.getWidth() - metrics.stringWidth(word)) / 2;
        yBaseLine = yBaseLine + metrics.getAscent();
        graphics.drawString(word, xBaseline, yBaseLine);

        xBaseline = (image.getWidth() - metrics.stringWidth(demoKeyCodeBottomString)) / 2;
        yBaseLine = yBaseLine + metrics.getAscent();
        graphics.drawString(demoKeyCodeBottomString, xBaseline, yBaseLine);

        graphics.dispose();

        final var dest = writeRaster(image);
        image.setData(dest);
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

    private Raster writeRaster(final BufferedImage image) {
        // Get the raster data (array of pixels)
        final var src = image.getData();

        // Create an identically-sized output raster
        final var dest = src.createCompatibleWritableRaster();

        // Loop through every pixelSize pixels, in both x and y directions
        for (int y = 0; y < src.getHeight(); y += PIXEL_SIZE) {
            for (int x = 0; x < src.getWidth(); x += PIXEL_SIZE) {
                // Copy the pixel
                double[] pixel = new double[dest.getNumBands()];
                pixel = src.getPixel(x, y, pixel);

                // "Paste" the pixel onto the surrounding pixelSize by pixelSize neighbors
                // Also make sure that our loop never goes outside the bounds of the image
                for (int yd = y; (yd < y + PIXEL_SIZE) && (yd < dest.getHeight()); yd++) {
                    for (int xd = x; (xd < x + PIXEL_SIZE) && (xd < dest.getWidth()); xd++) {
                        dest.setPixel(xd, yd, pixel);
                    }
                }
            }
        }

        return dest;
    }
}
