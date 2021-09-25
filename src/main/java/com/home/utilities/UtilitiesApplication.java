package com.home.utilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
