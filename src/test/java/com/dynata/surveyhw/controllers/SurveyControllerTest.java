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
public class SurveyControllerTest extends BaseTest {

    @Test
    @Order(1)
    void uploadSurveyTestFile_valid() {
        RestAssured.given()
                .multiPart("file", new File("src/test/resources/testfiles/Surveys.csv"))
                .when()
                .post("/surveys")
                .then()
                .statusCode(201)
                .body("$", Matchers.hasSize(100));

        String findAllSize = RestAssured.post("/test-utils/findAllSurvey").asString();
        assertThat(findAllSize).isEqualTo("100");
    }

    @Test
    @Order(2)
    void getByMemberIdAndIsCompleted_valid() {
        RestAssured.post("/test-utils/init");

        RestAssured.when()
                .get("/surveys/by-member-id-and-completed?memberId=2")
                .then()
                .statusCode(200)
                .body("content", Matchers.hasSize(9))
                .body("content[0].surveyId", Matchers.equalTo(1))
                .body("content[0].name", Matchers.equalTo("Survey 01"))
                .body("content[0].expectedCompletes", Matchers.equalTo(30))
                .body("content[0].completionPoints", Matchers.equalTo(5))
                .body("content[0].filteredPoints", Matchers.equalTo(2));
    }

    @Test
    @Order(3)
    void getSurveyCompletionPointsByMemberId_valid() {
        RestAssured.post("/test-utils/init");

        RestAssured.when()
                .get("/surveys/by-member-id-completion-points?memberId=2")
                .then()
                .statusCode(200)
                .body("'Survey 01'", Matchers.equalTo(5))
                .body("'Survey 18'", Matchers.equalTo(25))
                .body("'Survey 30'", Matchers.equalTo(25))
                .body("'Survey 44'", Matchers.equalTo(35))
                .body("'Survey 49'", Matchers.equalTo(35))
                .body("'Survey 51'", Matchers.equalTo(15))
                .body("'Survey 60'", Matchers.equalTo(35))
                .body("'Survey 62'", Matchers.equalTo(40))
                .body("'Survey 65'", Matchers.equalTo(20))
                .body("'Survey 82'", Matchers.equalTo(35))
                .body("'Survey 85'", Matchers.equalTo(25))
                .body("'Survey 97'", Matchers.equalTo(35));
    }

    @Test
    @Order(4)
    void getAllStatisticSurveys_valid() {
        RestAssured.post("/test-utils/init");

        RestAssured.when()
                .get("/surveys/all-statistic?sort=surveyId,ASC")
                .then()
                .statusCode(200)
                .body("content", Matchers.hasSize(20))
                .body("content[1].surveyId", Matchers.equalTo(2))
                .body("content[1].name", Matchers.equalTo("Survey 02"))
                .body("content[1].numberOfCompletes", Matchers.equalTo(14))
                .body("content[1].numberOfFilteredParticipants", Matchers.equalTo(7))
                .body("content[1].numberOfRejectedParticipants", Matchers.equalTo(3))
                .body("content[1].averageLengthOfTimeSpentOnSurvey", Matchers.equalTo(15.214286F));
    }
}
