package com.home.utilities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = UtilitiesApplication.class)
@TestPropertySource(locations = {"classpath:application-localhost.properties"})
class UtilitiesApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
