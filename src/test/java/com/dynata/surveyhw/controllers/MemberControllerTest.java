package com.dynata.surveyhw.controllers;

import io.restassured.RestAssured;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@NoArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberControllerTest extends BaseTest {

    @Test
    @Order(1)
    void uploadMemberTestFile_valid() {
        RestAssured.given()
                .multiPart("file", new File("src/test/resources/testfiles/Members.csv"))
                .when()
                .post("/members")
                .then()
                .statusCode(201)
                .body("$", Matchers.hasSize(300));

        String findAllSize = RestAssured.post("/test-utils/findAllMember").asString();
        assertThat(findAllSize).isEqualTo("300");
    }

    @Test
    @Order(2)
    void getBySurveyIdAndIsCompleted_valid() {
        RestAssured.post("/test-utils/init");

        RestAssured.when()
                .get("/members/by-survey-and-completed?surveyId=2")
                .then()
                .statusCode(200)
                .body("content", Matchers.hasSize(14))
                .body("content[0].memberId", Matchers.equalTo(7))
                .body("content[0].fullName", Matchers.equalTo("Vada Shaeffer"))
                .body("content[0].emailAddress", Matchers.equalTo("VadaShaeffer8856@gmail.com"))
                .body("content[0].isActive", Matchers.equalTo(false));
    }

    @Test
    @Order(3)
    void getByNotParticipatedInSurveyAndIsActive_valid() {
        RestAssured.post("/test-utils/init");

        RestAssured.when()
                .get("/members/by-not-participated-survey-and-active?surveyId=2")
                .then()
                .statusCode(200)
                .body("content", Matchers.hasSize(2))
                .body("content[0].memberId", Matchers.equalTo(49))
                .body("content[0].fullName", Matchers.equalTo("Cherryl Carolina"))
                .body("content[0].emailAddress", Matchers.equalTo("CherrylCarolina2273@gmail.com"))
                .body("content[0].isActive", Matchers.equalTo(true));
    }
}
