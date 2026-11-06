package kz.narxoz.carrental.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import kz.narxoz.carrental.config.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Proves the values come from application-dev.yaml. No socket is opened. */
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
class DownstreamPropertiesTest {

    @Autowired
    private DownstreamProperties downstream;

    @Test
    void devValuesAreBound() {
        assertEquals("http://localhost:9090", downstream.baseUrl());
        assertEquals(Duration.ofSeconds(2), downstream.timeout());
    }
}
