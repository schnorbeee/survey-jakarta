package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Participation;

public interface ParticipationRepository extends GenericRepository<Participation, Long> {

    void upsertParticipation(Participation p);
}
