package com.home.utilities;

import com.home.keycode.configuration.EnableKeyCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableKeyCode
@SpringBootApplication
public class UtilitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UtilitiesApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
    }
}
