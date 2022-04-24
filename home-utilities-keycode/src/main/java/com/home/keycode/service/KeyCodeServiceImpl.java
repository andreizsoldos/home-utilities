package com.home.keycode.service;

import ij.ImagePlus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KeyCodeServiceImpl implements KeyCodeService {

    private static final String JPG = "jpg";
    private static final String COLOR = "#0D6EFD";
    private static final String KEYCODE_TITLE = "KeyCode";
    private static final String ORIGINAL_KEYCODE_PATH = "static/images/keycode/";
    private static final String ORIGINAL_KEYCODE_FILE_NAME = "keycode-background.jpg";
    private static final String GENERATED_KEYCODE_FILE_NAME = "keycode.jpg";
    private static final long KEYCODE_SECURITY_CHARACTER_LENGTH = 10L;
    private static final double ANGLE = 0;
    private static final String SPACE = " ";
    private static final String ENCODED_SPACE = "%20";

    @Value("${application.keycode.pixel.size}")
    private int pixelSize;

    @Value("${application.keycode.text.size}")
    private int textSize;

    @Value("${application.keycode.text.length}")
    private long textLength;

    @Override
    public void generateKeyCode() throws IOException {
        new ImagePlus(KEYCODE_TITLE, writeToKeyCode(saveOutputFile()));
    }

    private BufferedImage writeToKeyCode(final File outputFile) throws IOException {
        System.out.println(getInputFile());
        final var image = ImageIO.read(getInputFile());
        final var font = new Font("Arial Bold", Font.ITALIC, textSize);

        final var graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.decode(COLOR));
        final var metrics = graphics.getFontMetrics(font);

        final var keyCode = getKeyCodeText(image, font, graphics, metrics);

        // Save the raster back to the Image
        final var dest = writeRaster(image);
        image.setData(dest);

        // Write the new file
        ImageIO.write(image, JPG, outputFile);

        return image;
    }

    private String getKeyCodeText(final BufferedImage image, final Font font, final Graphics2D graphics, final FontMetrics metrics) {
        final var affine = new AffineTransform();
        affine.rotate(Math.toRadians(ANGLE), 0, 0);
        var rotatedFont = font.deriveFont(affine);
        graphics.setFont(rotatedFont);

        int positionX;
        final var demoKeyCodeTopString = String.join(" ", randomKeyCodeText(KEYCODE_SECURITY_CHARACTER_LENGTH));
        positionX = (image.getWidth() - metrics.stringWidth(demoKeyCodeTopString)) / 2;
        graphics.drawString(demoKeyCodeTopString, positionX, metrics.getAscent());

        final var keyCodeString = String.join(" ", randomKeyCodeText(textLength));
        positionX = (image.getWidth() - metrics.stringWidth(keyCodeString)) / 2;
        graphics.drawString(keyCodeString, positionX, ((image.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent());

        final var demoKeyCodeBtootmString = String.join(" ", randomKeyCodeText(KEYCODE_SECURITY_CHARACTER_LENGTH));
        positionX = (image.getWidth() - metrics.stringWidth(demoKeyCodeBtootmString)) / 2;
        graphics.drawString(demoKeyCodeBtootmString, positionX, image.getHeight() - (metrics.getAscent() / 2));

        graphics.dispose();

        return keyCodeString;
    }

    private Raster writeRaster(final BufferedImage image) {
        // Get the raster data (array of pixels)
        final var src = image.getData();

        // Create an identically-sized output raster
        final var dest = src.createCompatibleWritableRaster();

        // Loop through every pixelSize pixels, in both x and y directions
        for (int y = 0; y < src.getHeight(); y += pixelSize) {
            for (int x = 0; x < src.getWidth(); x += pixelSize) {
                // Copy the pixel
                double[] pixel = new double[3];
                pixel = src.getPixel(x, y, pixel);

                // "Paste" the pixel onto the surrounding pixelSize by pixelSize neighbors
                // Also make sure that our loop never goes outside the bounds of the image
                for (int yd = y; (yd < y + pixelSize) && (yd < dest.getHeight()); yd++) {
                    for (int xd = x; (xd < x + pixelSize) && (xd < dest.getWidth()); xd++) {
                        dest.setPixel(xd, yd, pixel);
                    }
                }
            }
        }
        return dest;
    }

    private File getInputFile() {
        final var path = Objects.requireNonNull(this.getClass().getResource("/".concat(ORIGINAL_KEYCODE_PATH))).getPath();
        return Paths.get(sanitizePath(path).concat(ORIGINAL_KEYCODE_FILE_NAME).replace("file:/","")).toFile();
    }

    private File saveOutputFile() {
        final var path = Objects.requireNonNull(this.getClass().getResource("/".concat(ORIGINAL_KEYCODE_PATH))).getPath();
        return new File(sanitizePath(path), GENERATED_KEYCODE_FILE_NAME);
    }

    private String sanitizePath(final String path) {
        if (path.startsWith("/")) {
            return path.substring(1).replace(ENCODED_SPACE, SPACE);
        }
        return path.replace(ENCODED_SPACE, SPACE);
    }

    private List<String> randomKeyCodeText(final long textLength) {
        final var asciiNumbersRange = new int[]{48, 57};    // numbers
        final var asciiCapitalLettersRange = new int[]{65, 90};   // capital letters
        final var asciiSmallLettersRange = new int[]{97, 122};   // small letters

        final var numbers = IntStream.rangeClosed(asciiNumbersRange[0], asciiNumbersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(numbers);

        final var capitalLetters = IntStream.rangeClosed(asciiCapitalLettersRange[0], asciiCapitalLettersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(capitalLetters);
        numbers.addAll(capitalLetters);
        Collections.shuffle(numbers);

        final var smallLetters = IntStream.rangeClosed(asciiSmallLettersRange[0], asciiSmallLettersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(smallLetters);
        numbers.addAll(smallLetters);
        Collections.shuffle(numbers);

        final var secureRandom = new SecureRandom();
        return secureRandom.ints(textLength, 0, numbers.size() - 1)
              .boxed()
              .map(numbers::get)
              .collect(Collectors.toList());
    }
}
