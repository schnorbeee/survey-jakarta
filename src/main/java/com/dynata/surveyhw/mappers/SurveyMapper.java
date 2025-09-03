package com.dynata.surveyhw.mappers;

import com.dynata.surveyhw.dtos.SurveyDto;
import com.dynata.surveyhw.dtos.csv.SurveyCsvDto;
import com.dynata.surveyhw.entities.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface SurveyMapper {

    SurveyDto toDto(Survey entity);

    Survey toEntity(SurveyCsvDto dto);
}