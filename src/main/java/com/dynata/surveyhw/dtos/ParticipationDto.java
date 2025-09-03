package com.dynata.surveyhw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParticipationDto {

    private Long memberId;

    private Long surveyId;

    private Long statusId;

    private Integer length;
}
