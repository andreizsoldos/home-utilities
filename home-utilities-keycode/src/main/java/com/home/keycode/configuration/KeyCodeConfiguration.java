package com.home.keycode.configuration;

import com.home.keycode.filters.KeyCodeFilter;
import com.home.keycode.graphics.text.util.FilterUtilProperties;
import com.home.keycode.graphics.text.util.ImageUtilProperties;
import com.home.keycode.graphics.text.util.SecurityTextUtilProperties;
import com.home.keycode.graphics.text.util.TextUtilProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCodeConfiguration {

    private static final String KEYCODE_FILTER_PREFIX = "application.keycode.filter";
    private static final String KEYCODE_IMAGE_PREFIX = "application.keycode.image";
    private static final String KEYCODE_TEXT_PREFIX = "application.keycode.text";
    private static final String KEYCODE_SECURITY_TEXT_PREFIX = "application.keycode.security.text";

    @Bean
    @ConfigurationProperties(prefix = KEYCODE_FILTER_PREFIX)
    public FilterUtilProperties filterUtil() {
        return new FilterUtilProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = KEYCODE_IMAGE_PREFIX)
    public ImageUtilProperties imageUtil() {
        return new ImageUtilProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = KEYCODE_TEXT_PREFIX)
    public TextUtilProperties textUtil() {
        return new TextUtilProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = KEYCODE_SECURITY_TEXT_PREFIX)
    public SecurityTextUtilProperties securityTextUtil() {
        return new SecurityTextUtilProperties();
    }

    @Bean
    public KeyCodeFilter keyCodeFilter() {
        return new KeyCodeFilter();
    }
}
