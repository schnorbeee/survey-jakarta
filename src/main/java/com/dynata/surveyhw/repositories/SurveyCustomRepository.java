package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.entities.Survey;
import org.springframework.data.domain.Pageable;

public interface SurveyCustomRepository {

    PageDto<Survey> findByMemberIdAndIsCompleted(Long memberId, Pageable pageable);

    PageDto<SurveyStatisticDto> getAllStatisticSurveys(Pageable pageable);
}
