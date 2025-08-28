package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.StatusDto;
import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.mappers.StatusMapper;
import com.dynata.surveyhw.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    private final StatusMapper statusMapper;

    @Autowired
    public StatusService(StatusRepository statusRepository,
            StatusMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }

    public List<StatusDto> saveStatusDtos(List<StatusCsvDto> statusDtos) {
        return statusDtos.stream()
                .map(statusMapper::toEntity)
                .peek(statusRepository::upsertStatus)
                .map(statusMapper::toDto)
                .toList();
    }
}
