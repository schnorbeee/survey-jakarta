package com.dynata.surveyhw.mappers;

import com.dynata.surveyhw.dtos.ParticipationDto;
import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.entities.Participation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    ParticipationDto toDto(Participation entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "survey", ignore = true)
    @Mapping(target = "status", ignore = true)
    Participation toEntity(ParticipationCsvDto dto);
}
