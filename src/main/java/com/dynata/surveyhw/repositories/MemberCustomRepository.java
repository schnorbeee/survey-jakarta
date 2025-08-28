package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.entities.Member;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    PageDto<Member> findBySurveyIdAndIsCompleted(Long surveyId, Pageable pageable);

    PageDto<Member> findByNotParticipatedSurveyAndIsActive(Long surveyId, Pageable pageable);
}
