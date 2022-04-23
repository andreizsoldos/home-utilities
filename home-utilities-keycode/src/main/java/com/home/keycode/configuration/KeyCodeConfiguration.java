package com.home.keycode.configuration;

import com.home.keycode.filters.KeyCodeFilter;
import com.home.keycode.service.KeyCodeService;
import com.home.keycode.service.KeyCodeServiceImpl;
import com.home.keycode.validation.KeyCodeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCodeConfiguration {

    @Bean
    public KeyCodeService keyCodeService() {
        return new KeyCodeServiceImpl();
    }

    @Bean
    public KeyCodeValidator keyCodeValidator() {
        return null;
    }

    @Bean
    public KeyCodeFilter keyCodeFilter() {
        return new KeyCodeFilter();
    }
}
