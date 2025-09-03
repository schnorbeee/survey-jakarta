package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.ParticipationDto;
import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.mappers.ParticipationMapper;
import com.dynata.surveyhw.repositories.ParticipationRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ParticipationService {

    @EJB
    private ParticipationRepository participationRepository;

    @Inject
    private ParticipationMapper participationMapper;

    public List<ParticipationDto> saveParticipationDtos(List<ParticipationCsvDto> participationDtos) {
        return participationDtos.stream()
                .map(participationMapper::toEntity)
                .peek(participationRepository::upsertParticipation)
                .map(participationMapper::toDto)
                .toList();
    }
}
