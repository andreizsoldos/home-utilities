package com.home.keycode;

import com.home.keycode.graphics.backgrounds.BackgroundProducer;
import com.home.keycode.graphics.backgrounds.TransparentBackgroundProducer;
import com.home.keycode.graphics.gimpy.FishEyeGimpyRenderer;
import com.home.keycode.graphics.gimpy.GimpyRenderer;
import com.home.keycode.graphics.noise.CurvedLineNoiseProducer;
import com.home.keycode.graphics.noise.NoiseProducer;
import com.home.keycode.graphics.text.producer.DefaultTextProducer;
import com.home.keycode.graphics.text.producer.TextProducer;
import com.home.keycode.graphics.text.renderer.DefaultWordRenderer;
import com.home.keycode.graphics.text.renderer.WordRenderer;
import com.home.keycode.validation.KeyCodeValidator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Base64;

public final class KeyCode implements KeyCodeValidator, Serializable {

    public static final String NAME = "keyCode";

    @Serial
    private static final long serialVersionUID = 617511236L;
    private static final String PNG = "png";

    private final Builder builder;

    private KeyCode(final Builder builder) {
        this.builder = builder;
    }

    @Override
    public boolean validate(final String answer) {
        return answer.equals(builder.answer);
    }

    public String getAnswer() {
        return builder.answer;
    }

    public BufferedImage getImage() {
        return builder.image;
    }

    public byte[] toByteArray() throws IOException {
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(this.getImage(), PNG, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String toBase64() throws IOException {
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(this.getImage(), PNG, byteArrayOutputStream);
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static class Builder implements Serializable {

        @Serial
        private static final long serialVersionUID = 12L;

        private String answer;
        private boolean addBorder = false;
        private transient BufferedImage image;
        private transient BufferedImage backgroundImage;

        private final int textSize;

        public Builder(final int width, final int height, final int textSize) {
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            this.textSize = textSize;
        }

        public Builder addBackground() {
            return addBackground(new TransparentBackgroundProducer());
        }

        public Builder addBackground(final BackgroundProducer backgroundProducer) {
            this.backgroundImage = backgroundProducer.getBackground(image.getWidth(), image.getHeight());
            return this;
        }

        public Builder addText() {
            return addText(new DefaultTextProducer());
        }

        public Builder addText(final TextProducer textProducer) {
            return addText(textProducer, new DefaultWordRenderer(textSize));
        }

        public Builder addText(final WordRenderer wordRenderer) {
            return addText(new DefaultTextProducer(), wordRenderer);
        }

        public Builder addText(final TextProducer textProducer, final WordRenderer wordRenderer) {
            this.answer = textProducer.getText();
            wordRenderer.render(answer, image);
            return this;
        }

        public Builder addNoise() {
            return this.addNoise(new CurvedLineNoiseProducer());
        }

        public Builder addNoise(final NoiseProducer noiseProducer) {
            noiseProducer.makeNoise(image);
            return this;
        }

        public Builder gimp() {
            return gimp(new FishEyeGimpyRenderer());
        }

        public Builder gimp(final GimpyRenderer gimpyRenderer) {
            gimpyRenderer.gimp(image);
            return this;
        }

        public Builder addBorder() {
            this.addBorder = true;
            return this;
        }

        public KeyCode build() {
            if (backgroundImage == null) {
                backgroundImage = new TransparentBackgroundProducer().getBackground(image.getWidth(), image.getHeight());
            }

            final var graphics = backgroundImage.createGraphics();
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            graphics.drawImage(image, null, null);

            if (addBorder) {
                final var width = image.getWidth();
                final var height = image.getHeight();

                graphics.setColor(Color.BLACK);
                graphics.drawLine(0, 0, 0, width);
                graphics.drawLine(0, 0, width, 0);
                graphics.drawLine(0, height - 1, width, height - 1);
                graphics.drawLine(width - 1, height - 1, width - 1, 0);
            }

            this.image = backgroundImage;

            return new KeyCode(this);
        }
    }
}
