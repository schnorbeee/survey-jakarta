package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.entities.Member;
import com.dynata.surveyhw.entities.QMember;
import com.dynata.surveyhw.entities.QParticipation;
import com.dynata.surveyhw.repositories.MemberCustomRepository;
import com.dynata.surveyhw.utils.QuerySortUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final EntityManager em;

    @Override
    public PageDto<Member> findBySurveyIdAndIsCompleted(Long surveyId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember m = QMember.member;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable, m.getType());

        List<Member> members = queryFactory.selectFrom(m)
                .join(p).on(m.memberId.eq(p.memberId))
                .where(p.surveyId.eq(surveyId).and(p.statusId.eq(4L)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(m.count())
                .from(m)
                .join(p).on(m.memberId.eq(p.memberId))
                .where(p.surveyId.eq(surveyId).and(p.statusId.eq(4L)))
                .fetchOne();

        return new PageDto<>(members, pageable.getPageNumber(), pageable.getPageSize(), total);
    }

    @Override
    public PageDto<Member> findByNotParticipatedSurveyAndIsActive(Long surveyId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember m = QMember.member;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable, m.getType());

        List<Member> members = queryFactory.selectFrom(m)
                .join(p).on(m.memberId.eq(p.memberId))
                .where(p.surveyId.eq(surveyId).and(p.statusId.eq(1L).or(p.statusId.eq(2L))).and(m.isActive.eq(true)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(m.count())
                .from(m)
                .join(p).on(m.memberId.eq(p.memberId))
                .where(p.surveyId.eq(surveyId).and(p.statusId.eq(1L).or(p.statusId.eq(2L))).and(m.isActive.eq(true)))
                .fetchOne();

        return new PageDto<>(members, pageable.getPageNumber(), pageable.getPageSize(), total);
    }
}
