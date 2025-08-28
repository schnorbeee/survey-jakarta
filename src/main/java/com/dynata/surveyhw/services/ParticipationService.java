package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.ParticipationDto;
import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.mappers.ParticipationMapper;
import com.dynata.surveyhw.repositories.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;

    private final ParticipationMapper participationMapper;

    @Autowired
    public ParticipationService(ParticipationRepository participationRepository,
            ParticipationMapper participationMapper) {
        this.participationRepository = participationRepository;
        this.participationMapper = participationMapper;
    }

    public List<ParticipationDto> saveParticipationDtos(List<ParticipationCsvDto> participationDtos) {
        return participationDtos.stream()
                .map(participationMapper::toEntity)
                .peek(participationRepository::upsertParticipation)
                .map(participationMapper::toDto)
                .toList();
    }
}
