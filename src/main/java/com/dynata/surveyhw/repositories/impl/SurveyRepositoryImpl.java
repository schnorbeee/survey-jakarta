package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.SurveyStatisticDto;
import com.dynata.surveyhw.entities.QParticipation;
import com.dynata.surveyhw.entities.QSurvey;
import com.dynata.surveyhw.entities.Survey;
import com.dynata.surveyhw.repositories.SurveyRepository;
import com.dynata.surveyhw.utils.QuerySortUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Stateless
@Transactional
public class SurveyRepositoryImpl extends GenericRepositoryImpl<Survey, Long> implements SurveyRepository {

    private static final String SURVEY_DB_ID = "survey_id";
    private static final String SURVEY_DB_NAME = "name";
    private static final String SURVEY_DB_EXPECTED_COMPLETES = "expected_completes";
    private static final String SURVEY_DB_COMPLETION_POINTS = "completion_points";
    private static final String SURVEY_DB_FILTERED_POINTS = "filtered_points";

    private static final String STATISTIC_DB_ID = "surveyId";
    private static final String STATISTIC_DB_NAME = "name";
    private static final String STATISTIC_DB_NUMBER_OF_COMPLETES = "numberOfCompletes";
    private static final String STATISTIC_DB_NUMBER_OF_FILTERED = "numberOfFilteredParticipants";
    private static final String STATISTIC_DB_NUMBER_OF_REJECTED = "numberOfRejectedParticipants";
    private static final String STATISTIC_DB_AVERAGE_LENGTH = "averageLengthOfTimeSpentOnSurvey";

    private static final Map<String, String> SORT_COLUMN_MAP = Map.of(
            "surveyId", SURVEY_DB_ID,
            "name", SURVEY_DB_NAME,
            "expectedCompletes", SURVEY_DB_EXPECTED_COMPLETES,
            "completionPoints", SURVEY_DB_COMPLETION_POINTS,
            "filteredPoints", SURVEY_DB_FILTERED_POINTS
    );

    private static final Map<String, String> SORT_COLUMN_MAP_STATISTIC = Map.of(
            "surveyId", STATISTIC_DB_ID,
            "name", STATISTIC_DB_NAME,
            "numberOfCompletes", STATISTIC_DB_NUMBER_OF_COMPLETES,
            "numberOfFilteredParticipants", STATISTIC_DB_NUMBER_OF_FILTERED,
            "numberOfRejectedParticipants", STATISTIC_DB_NUMBER_OF_REJECTED,
            "averageLengthOfTimeSpentOnSurvey", STATISTIC_DB_AVERAGE_LENGTH
    );

    public SurveyRepositoryImpl() {
        super(Survey.class);
    }

    @Override
    public void upsertSurvey(Survey s) {
        em.createNativeQuery("""
                        INSERT INTO survey (survey_id, name, expected_completes, completion_points, filtered_points)
                        VALUES (:surveyId, :name, :expectedCompletes, :completionPoints, :filteredPoints)
                        ON CONFLICT (survey_id)
                        DO UPDATE SET name = EXCLUDED.name,
                                    expected_completes = EXCLUDED.expected_completes,
                                    completion_points = EXCLUDED.completion_points,
                                    filtered_points = EXCLUDED.filtered_points
                        """)
                .setParameter("surveyId", s.getSurveyId())
                .setParameter("name", s.getName())
                .setParameter("expectedCompletes", s.getExpectedCompletes())
                .setParameter("completionPoints", s.getCompletionPoints())
                .setParameter("filteredPoints", s.getFilteredPoints())
                .executeUpdate();
    }

    @Override
    public List<Survey> findCompletionPointsByMemberId(Long memberId) {
        return em.createQuery("""
                        SELECT s FROM Survey s JOIN Participation p ON s.surveyId = p.survey.surveyId WHERE p.member.memberId = :memberId
                        """, Survey.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public PageDto<Survey> findByMemberIdAndIsCompleted(Long memberId, PageRequest pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QSurvey s = QSurvey.survey;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable.getSort(), s.getType());

        List<Survey> surveys = queryFactory.selectFrom(s)
                .join(p).on(s.surveyId.eq(p.survey.surveyId))
                .where(p.member.memberId.eq(memberId).and(p.status.statusId.eq(4L)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(s.count())
                .from(s)
                .join(p).on(s.surveyId.eq(p.survey.surveyId))
                .where(p.member.memberId.eq(memberId).and(p.status.statusId.eq(4L)))
                .fetchOne();

        return new PageDto<>(surveys, pageable.getPageNumber(), pageable.getPageSize(), total);
    }

    @Override
    public PageDto<SurveyStatisticDto> getAllStatisticSurveys(PageRequest pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QSurvey s = QSurvey.survey;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable.getSort(), s.getType());

        NumberExpression<Integer> statusCompletedCount = new CaseBuilder().when(p.status.statusId.eq(4L))
                .then(1).otherwise(0).sum();
        NumberExpression<Integer> statusRejectedCount = new CaseBuilder().when(p.status.statusId.eq(2L))
                .then(1).otherwise(0).sum();
        NumberExpression<Integer> statusFilteredCount = new CaseBuilder().when(p.status.statusId.eq(3L))
                .then(1).otherwise(0).sum();
        NumberExpression<Double> statusCompletedLengthCount = new CaseBuilder().when(p.status.statusId.eq(4L))
                .then(p.length).otherwise((Integer) null).avg();

        List<SurveyStatisticDto> surveyStatisticList = queryFactory
                .select(Projections.bean(SurveyStatisticDto.class,
                        s.surveyId.as(STATISTIC_DB_ID),
                        s.name.as(STATISTIC_DB_NAME),
                        statusCompletedCount.as(STATISTIC_DB_NUMBER_OF_COMPLETES),
                        statusRejectedCount.as(STATISTIC_DB_NUMBER_OF_REJECTED),
                        statusFilteredCount.as(STATISTIC_DB_NUMBER_OF_FILTERED),
                        statusCompletedLengthCount.as(STATISTIC_DB_AVERAGE_LENGTH)))
                .from(s)
                .join(p).on(s.surveyId.eq(p.survey.surveyId))
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
