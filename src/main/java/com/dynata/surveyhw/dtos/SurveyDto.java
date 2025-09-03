package com.dynata.surveyhw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SurveyDto {

    private Long surveyId;

    private String name;

    private Integer expectedCompletes;

    private Integer completionPoints;

    private Integer filteredPoints;
}
