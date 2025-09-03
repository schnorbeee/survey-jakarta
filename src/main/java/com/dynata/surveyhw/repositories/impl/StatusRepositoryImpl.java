package com.dynata.surveyhw.repositories.impl;

import com.dynata.surveyhw.entities.Status;
import com.dynata.surveyhw.repositories.StatusRepository;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

@Stateless
@Transactional
public class StatusRepositoryImpl extends GenericRepositoryImpl<Status, Long> implements StatusRepository {

    public StatusRepositoryImpl() {
        super(Status.class);
    }

    @Override
    public void upsertStatus(Status s) {
        em.createNativeQuery("""
                            INSERT INTO status (status_id, name)
                            VALUES (:statusId, :name)
                            ON CONFLICT (status_id)
                            DO UPDATE SET name = EXCLUDED.name
                        """)
                .setParameter("statusId", s.getStatusId())
                .setParameter("name", s.getName())
                .executeUpdate();
    }
}
