package com.home.utilities.configuration.keycode;

import com.home.keycode.service.KeyCodeService;
import com.home.keycode.service.KeyCodeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCodeConfig {

    @Bean
    public KeyCodeService keyCodeService() {
        return new KeyCodeServiceImpl();
    }
}
