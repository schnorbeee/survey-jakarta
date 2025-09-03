package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.StatusDto;
import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.mappers.StatusMapper;
import com.dynata.surveyhw.repositories.StatusRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class StatusService {

    @EJB
    private StatusRepository statusRepository;

    @Inject
    private StatusMapper statusMapper;

    public List<StatusDto> saveStatusDtos(List<StatusCsvDto> statusDtos) {
        return statusDtos.stream()
                .map(statusMapper::toEntity)
                .peek(statusRepository::upsertStatus)
                .map(statusMapper::toDto)
                .toList();
    }
}
