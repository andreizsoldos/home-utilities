package com.home.keycode.graphics.backgrounds;

import java.awt.image.BufferedImage;

public interface BackgroundProducer {

    BufferedImage addBackground(BufferedImage image);
    
    BufferedImage getBackground(int width, int height);
}
