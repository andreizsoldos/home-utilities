package com.home.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class UtilitiesApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
    }

    public static void main(String[] args) {
        SpringApplication.run(UtilitiesApplication.class, args);
    }
}
