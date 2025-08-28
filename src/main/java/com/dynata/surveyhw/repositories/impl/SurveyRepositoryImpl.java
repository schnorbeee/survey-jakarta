package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.entities.QParticipation;
import com.dynata.surveyhw.entities.QSurvey;
import com.dynata.surveyhw.entities.Survey;
import com.dynata.surveyhw.repositories.SurveyCustomRepository;
import com.dynata.surveyhw.utils.QuerySortUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyCustomRepository {

    private final EntityManager em;

    @Override
    public PageDto<Survey> findByMemberIdAndIsCompleted(Long memberId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QSurvey s = QSurvey.survey;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable, s.getType());

        List<Survey> surveys = queryFactory.selectFrom(s)
                .join(p).on(s.surveyId.eq(p.surveyId))
                .where(p.memberId.eq(memberId).and(p.statusId.eq(4L)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(s.count())
                .from(s)
                .join(p).on(s.surveyId.eq(p.surveyId))
                .where(p.memberId.eq(memberId).and(p.statusId.eq(4L)))
                .fetchOne();

        return new PageDto<>(surveys, pageable.getPageNumber(), pageable.getPageSize(), total);
    }

    @Override
    public PageDto<SurveyStatisticDto> getAllStatisticSurveys(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QSurvey s = QSurvey.survey;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable, s.getType());

        NumberExpression<Integer> statusCompletedCount = new CaseBuilder().when(p.statusId.eq(4L))
                .then(1).otherwise(0).sum();
        NumberExpression<Integer> statusRejectedCount = new CaseBuilder().when(p.statusId.eq(2L))
                .then(1).otherwise(0).sum();
        NumberExpression<Integer> statusFilteredCount = new CaseBuilder().when(p.statusId.eq(3L))
                .then(1).otherwise(0).sum();
        NumberExpression<Double> statusCompletedLengthCount = new CaseBuilder().when(p.statusId.eq(4L))
                .then(p.length).otherwise((Integer) null).avg();

        List<SurveyStatisticDto> surveyStatisticList = queryFactory
                .select(Projections.bean(SurveyStatisticDto.class,
                        s.surveyId.as("surveyId"),
                        s.name.as("name"),
                        statusCompletedCount.as("numberOfCompletes"),
                        statusRejectedCount.as("numberOfRejectedParticipants"),
                        statusFilteredCount.as("numberOfFilteredParticipants"),
                        statusCompletedLengthCount.as("averageLengthOfTimeSpentOnSurvey")))
                .from(s)
                .join(p).on(s.surveyId.eq(p.surveyId))
                .groupBy(s.surveyId, s.name)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(s.surveyId.count())
                .from(s)
                .fetchOne();

        return new PageDto<>(surveyStatisticList, pageable.getPageNumber(), pageable.getPageSize(), total);
    }
}
