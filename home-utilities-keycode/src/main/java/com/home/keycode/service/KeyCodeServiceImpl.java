package com.home.keycode.service;

import com.home.keycode.util.KeyCodeList;
import ij.ImagePlus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.AttributedString;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KeyCodeServiceImpl implements KeyCodeService {

    private static final String JPG = "jpg";
    private static final String COLOR = "#0D6EFD";
    private static final String KEYCODE_TITLE = "KeyCode";
    private static final String ORIGINAL_KEYCODE_PATH = "static/images/keycode/keycode-background.jpg";

    @Value("${application.keycode.pixel.size}")
    private int pixelSize;

    @Value("${application.keycode.text.size}")
    private int textSize;

    @Value("${application.keycode.text.length}")
    private long textLength;

    @Override
    public void generateKeyCode(final File outputFile) throws IOException {
        new ImagePlus(KEYCODE_TITLE, writeToKeyCode(outputFile));
    }

    private BufferedImage writeToKeyCode(final File outputFile) throws IOException {
        final var image = ImageIO.read(getInputFile());
        final var font = new Font("Arial Bold", Font.ITALIC, textSize);
        final var text = randomKeyCodeText();
        final var attributedText = new AttributedString(text);
        attributedText.addAttribute(TextAttribute.FONT, font);
        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.decode(COLOR));

        final var graphics = image.getGraphics();
        final var metrics = graphics.getFontMetrics(font);

        final int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
        final int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        graphics.drawString(attributedText.getIterator(), positionX, positionY);

        // Save the raster back to the Image
        final var dest = writeRaster(image);
        image.setData(dest);

        // Write the new file
        ImageIO.write(image, JPG, outputFile);

        return image;
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

    private File getInputFile() throws IOException {
        return new ClassPathResource(ORIGINAL_KEYCODE_PATH).getFile();
    }

    private String randomKeyCodeText() {
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

        final var smallLetters = IntStream.rangeClosed(asciiSmallLettersRange[0], asciiSmallLettersRange[1])
              .boxed()
              .map(Character::toString)
              .collect(Collectors.toList());
        Collections.shuffle(smallLetters);

        final var keyCodeList = new KeyCodeList.KeyCodeBuilder<String>()
              .addAll(numbers)
              .addAll(capitalLetters)
              .addAll(smallLetters)
              .getList();
        Collections.shuffle(keyCodeList);

        final var secureRandom = new SecureRandom();
        return secureRandom.ints(textLength, 0, keyCodeList.size() - 1)
              .boxed()
              .map(keyCodeList::get)
              .collect(Collectors.joining(" "));
    }
}
