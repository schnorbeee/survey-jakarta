package com.dynata.surveyhw.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SurveyStatisticDto {

    private Long surveyId;

    private String name;

    private Integer numberOfCompletes;

    private Integer numberOfFilteredParticipants;

    private Integer numberOfRejectedParticipants;

    private Double averageLengthOfTimeSpentOnSurvey;
}
