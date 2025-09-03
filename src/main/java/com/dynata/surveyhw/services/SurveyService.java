package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.SurveyDto;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.dtos.csv.SurveyCsvDto;
import com.dynata.surveyhw.mappers.PageMapper;
import com.dynata.surveyhw.mappers.SurveyMapper;
import com.dynata.surveyhw.repositories.SurveyRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class SurveyService {

    @EJB
    private SurveyRepository surveyRepository;

    @Inject
    private SurveyMapper surveyMapper;

    @Inject
    private PageMapper pageMapper;

    public List<SurveyDto> saveSurveyDtos(List<SurveyCsvDto> surveyDtos) {
        return surveyDtos.stream()
                .map(surveyMapper::toEntity)
                .peek(surveyRepository::upsertSurvey)
                .map(surveyMapper::toDto)
                .toList();
    }

    public PageDto<SurveyDto> getByMemberIdAndIsCompleted(Long memberId, PageRequest pageable) {
        return pageMapper.surveyDtos(surveyRepository.findByMemberIdAndIsCompleted(memberId, pageable));
    }

    public Map<String, Integer> getSurveyCompletionPointsByMemberId(Long memberId) {
        Map<String, Integer> surveyCompletionPoints = new HashMap<>();
        surveyRepository.findCompletionPointsByMemberId(memberId)
                .forEach(survey -> surveyCompletionPoints.put(survey.getName(), survey.getCompletionPoints()));
        return surveyCompletionPoints;
    }

    public PageDto<SurveyStatisticDto> getAllStatisticSurveys(PageRequest pageable) {
        return surveyRepository.getAllStatisticSurveys(pageable);
    }
}
