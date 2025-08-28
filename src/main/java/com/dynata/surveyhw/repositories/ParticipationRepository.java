package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Participation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO participation (member_id, survey_id, status_id, length)
            VALUES (:#{#p.memberId}, :#{#p.surveyId}, :#{#p.statusId}, :#{#p.length})
            ON CONFLICT (member_id, survey_id, status_id)
            DO UPDATE SET length = EXCLUDED.length
            """, nativeQuery = true)
    void upsertParticipation(Participation p);
}
