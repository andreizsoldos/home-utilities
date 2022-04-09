package com.home.keycode.service;

import java.io.File;
import java.io.IOException;

public interface KeyCodeService {

    void generateKeyCode(File outputFile) throws IOException;
}
