package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.entities.Member;

public interface MemberCustomRepository {

    PageDto<Member> findBySurveyIdAndIsCompleted(Long surveyId, PageRequest pageable);

    PageDto<Member> findByNotParticipatedSurveyAndIsActive(Long surveyId, PageRequest pageable);
}
