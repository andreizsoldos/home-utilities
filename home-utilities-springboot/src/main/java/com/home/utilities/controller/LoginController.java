package com.home.utilities.controller;

import com.home.keycode.KeyCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.home.keycode.KeyCode.NAME;

@Controller
public class LoginController {

    @ResponseBody
    @GetMapping(value = "/images/keycode")
    public String getKeyCode(final HttpSession httpSession) throws IOException {
        final var keyCode = (KeyCode) httpSession.getAttribute(NAME);
        return keyCode.toBase64();
    }
}
