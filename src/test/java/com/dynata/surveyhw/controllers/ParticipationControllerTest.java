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
public class ParticipationControllerTest extends BaseTest {

    @Test
    void uploadParticipationTestFile_valid() {
        RestAssured.post("/test-utils/init-except");

        RestAssured.given()
                .multiPart("file", new File("src/test/resources/testfiles/Participations.csv"))
                .when()
                .post("/participations")
                .then()
                .statusCode(201)
                .body("$", Matchers.hasSize(3000));

        String findAllSize = RestAssured.post("/test-utils/findAllParticipation").asString();
        assertThat(findAllSize).isEqualTo("3000");
    }
}
