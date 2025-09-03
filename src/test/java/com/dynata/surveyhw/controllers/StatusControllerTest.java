package com.dynata.surveyhw.controllers;

import io.restassured.RestAssured;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@NoArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StatusControllerTest extends BaseTest {

    @Test
    void uploadStatusTestFile_valid() {
        RestAssured.given()
                .multiPart("file", new File("src/test/resources/testfiles/Statuses.csv"))
                .when()
                .post("/statuses")
                .then()
                .statusCode(201)
                .body("$", Matchers.hasSize(4));

        String findAllSize = RestAssured.post("/test-utils/findAllStatus").asString();
        assertThat(findAllSize).isEqualTo("4");
    }
}
