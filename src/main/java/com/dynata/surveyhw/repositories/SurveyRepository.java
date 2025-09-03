package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Survey;

import java.util.List;

public interface SurveyRepository extends GenericRepository<Survey, Long>, SurveyCustomRepository {

    void upsertSurvey(Survey s);

    List<Survey> findCompletionPointsByMemberId(Long memberId);
}
