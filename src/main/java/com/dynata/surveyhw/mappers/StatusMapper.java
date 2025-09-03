package com.dynata.surveyhw.mappers;

import com.dynata.surveyhw.dtos.StatusDto;
import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.entities.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface StatusMapper {

    StatusDto toDto(Status entity);

    Status toEntity(StatusCsvDto dto);
}