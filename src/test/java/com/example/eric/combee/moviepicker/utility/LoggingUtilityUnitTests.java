package com.example.eric.combee.moviepicker.utility;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ActiveProfiles("test")
class LoggingUtilityUnitTests {

    @Test
    @Tag("unitTests")
    void logInfoSuccessTest() {
    }

    @Test
    @Tag("unitTests")
    void logErrorSuccessTest() {
    }

    @Test
    @Tag("unitTests")
    void logWebClientErrorSuccessTest() {
    }
}