package com.home.keycode.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(KeyCodeConfiguration.class)
public @interface EnableKeyCode {

}
