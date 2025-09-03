package com.dynata.surveyhw.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;

import static com.dynata.surveyhw.configurations.SharedContainers.WILDFLY;

public abstract class BaseTest {

    @BeforeAll
    static void setup() {
        WILDFLY.start();
        
        RestAssured.baseURI = "http://" + WILDFLY.getHost();
        RestAssured.port = WILDFLY.getMappedPort(8080);
        RestAssured.basePath = "/api";

        Awaitility.await().atMost(Duration.ofSeconds(120)).pollInterval(Duration.ofSeconds(2))
                .until(() -> {
                    try {
                        var r = RestAssured.get("/test-utils/ping");
                        System.out.println("Ping try: " + r.getStatusCode());
                        return r.getStatusCode() == 200;
                    } catch (Exception e) {
                        System.out.println("Ping failed: " + e.getMessage());
                        return false;
                    }
                });
    }

    @AfterEach
    void cleanup() {
        RestAssured.post("/test-utils/cleanup");
    }
}
