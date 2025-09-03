package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.entities.Member;
import com.dynata.surveyhw.entities.QMember;
import com.dynata.surveyhw.entities.QParticipation;
import com.dynata.surveyhw.repositories.MemberRepository;
import com.dynata.surveyhw.utils.QuerySortUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@Stateless
@Transactional
public class MemberRepositoryImpl extends GenericRepositoryImpl<Member, Long> implements MemberRepository {

    private static final String MEMBER_DB_ID = "member_id";
    private static final String MEMBER_DB_FULL_NAME = "full_name";
    private static final String MEMBER_DB_EMAIL_ADDRESS = "email_address";
    private static final String MEMBER_DB_IS_ACTIVE = "is_active";

    private static final Map<String, String> SORT_COLUMN_MAP = Map.of(
            "memberId", MEMBER_DB_ID,
            "fullName", MEMBER_DB_FULL_NAME,
            "emailAddress", MEMBER_DB_EMAIL_ADDRESS,
            "isActive", MEMBER_DB_IS_ACTIVE
    );

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public void upsertMember(Member m) {
        em.createNativeQuery("""
                            INSERT INTO member (member_id, full_name, email_address, is_active)
                            VALUES (:id, :fullName, :email, :active)
                            ON CONFLICT (member_id)
                            DO UPDATE SET full_name = EXCLUDED.full_name,
                                          email_address = EXCLUDED.email_address,
                                          is_active = EXCLUDED.is_active
                        """)
                .setParameter("id", m.getMemberId())
                .setParameter("fullName", m.getFullName())
                .setParameter("email", m.getEmailAddress())
                .setParameter("active", m.getIsActive())
                .executeUpdate();
    }

    public PageDto<Member> findBySurveyIdAndIsCompleted(Long surveyId, PageRequest pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember m = QMember.member;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable.getSort(), m.getType());

        List<Member> members = queryFactory.selectFrom(m)
                .join(p).on(m.memberId.eq(p.member.memberId))
                .where(p.survey.surveyId.eq(surveyId).and(p.status.statusId.eq(4L)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(m.count())
                .from(m)
                .join(p).on(m.memberId.eq(p.member.memberId))
                .where(p.survey.surveyId.eq(surveyId).and(p.status.statusId.eq(4L)))
                .fetchOne();

        return new PageDto<>(members, pageable.getPageNumber(), pageable.getPageSize(), total);
    }

    @Override
    public PageDto<Member> findByNotParticipatedSurveyAndIsActive(Long surveyId, PageRequest pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember m = QMember.member;
        QParticipation p = QParticipation.participation;

        List<OrderSpecifier<?>> orderSpecifiers = QuerySortUtils.toOrderSpecifiers(pageable.getSort(), m.getType());

        List<Member> members = queryFactory.selectFrom(m)
                .join(p).on(m.memberId.eq(p.member.memberId))
                .where(p.survey.surveyId.eq(surveyId)
                        .and(p.status.statusId.eq(1L).or(p.status.statusId.eq(2L)))
                        .and(m.isActive.eq(true)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(m.count())
                .from(m)
                .join(p).on(m.memberId.eq(p.member.memberId))
                .where(p.survey.surveyId.eq(surveyId)
                        .and(p.status.statusId.eq(1L).or(p.status.statusId.eq(2L)))
                        .and(m.isActive.eq(true)))
                .fetchOne();

        return new PageDto<>(members, pageable.getPageNumber(), pageable.getPageSize(), total);
    }
}
