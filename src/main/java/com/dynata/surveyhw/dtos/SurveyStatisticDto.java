package com.dynata.surveyhw.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Survey Statistic record")
public class SurveyStatisticDto {

    @Schema(description = "surveyId", example = "1")
    private Long surveyId;

    @Schema(description = "name", example = "Survey 01")
    private String name;

    @Schema(description = "numberOfCompletes", example = "15")
    private Integer numberOfCompletes;

    @Schema(description = "numberOfFilteredParticipants", example = "6")
    private Integer numberOfFilteredParticipants;

    @Schema(description = "numberOfRejectedParticipants", example = "1")
    private Integer numberOfRejectedParticipants;

    @Schema(description = "averageLengthOfTimeSpentOnSurvey", example = "1.5")
    private Double averageLengthOfTimeSpentOnSurvey;
}
