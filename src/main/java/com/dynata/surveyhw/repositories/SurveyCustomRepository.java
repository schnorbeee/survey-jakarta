package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.entities.Survey;

public interface SurveyCustomRepository {

    PageDto<Survey> findByMemberIdAndIsCompleted(Long memberId, PageRequest pageable);

    PageDto<SurveyStatisticDto> getAllStatisticSurveys(PageRequest pageable);
}
