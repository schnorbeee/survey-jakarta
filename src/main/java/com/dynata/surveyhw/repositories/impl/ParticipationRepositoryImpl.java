package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.entities.Participation;
import com.dynata.surveyhw.repositories.ParticipationRepository;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

@Stateless
@Transactional
public class ParticipationRepositoryImpl extends GenericRepositoryImpl<Participation, Long>
        implements ParticipationRepository {

    public ParticipationRepositoryImpl() {
        super(Participation.class);
    }

    @Override
    public void upsertParticipation(Participation p) {
        em.createNativeQuery("""
                            INSERT INTO participation (member_id, survey_id, status_id, length)
                            VALUES (:memberId, :surveyId, :statusId, :length)
                            ON CONFLICT (member_id, survey_id, status_id)
                            DO UPDATE SET length = EXCLUDED.length
                        """)
                .setParameter("memberId", p.getMember().getMemberId())
                .setParameter("surveyId", p.getSurvey().getSurveyId())
                .setParameter("statusId", p.getStatus().getStatusId())
                .setParameter("length", p.getLength())
                .executeUpdate();
    }
}
