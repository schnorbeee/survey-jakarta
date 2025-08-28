package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.SurveyDto;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.dtos.csv.SurveyCsvDto;
import com.dynata.surveyhw.mappers.PageMapper;
import com.dynata.surveyhw.mappers.SurveyMapper;
import com.dynata.surveyhw.repositories.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    private final SurveyMapper surveyMapper;

    private final PageMapper pageMapper;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository,
            SurveyMapper surveyMapper, PageMapper pageMapper) {
        this.surveyRepository = surveyRepository;
        this.surveyMapper = surveyMapper;
        this.pageMapper = pageMapper;
    }

    public List<SurveyDto> saveSurveyDtos(List<SurveyCsvDto> surveyDtos) {
        return surveyDtos.stream()
                .map(surveyMapper::toEntity)
                .peek(surveyRepository::upsertSurvey)
                .map(surveyMapper::toDto)
                .toList();
    }

    public PageDto<SurveyDto> getByMemberIdAndIsCompleted(Long memberId, Pageable pageable) {
        return pageMapper.surveyDtos(surveyRepository.findByMemberIdAndIsCompleted(memberId, pageable));
    }

    public Map<String, Integer> getSurveyCompletionPointsByMemberId(Long memberId) {
        Map<String, Integer> surveyCompletionPoints = new HashMap<>();
        surveyRepository.findCompletionPointsByMemberId(memberId)
                .forEach(survey -> surveyCompletionPoints.put(survey.getName(), survey.getCompletionPoints()));
        return surveyCompletionPoints;
    }

    public PageDto<SurveyStatisticDto> getAllStatisticSurveys(Pageable pageable) {
        return surveyRepository.getAllStatisticSurveys(pageable);
    }
}
