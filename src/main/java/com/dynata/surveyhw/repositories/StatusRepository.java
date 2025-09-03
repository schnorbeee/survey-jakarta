package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Status;

public interface StatusRepository extends GenericRepository<Status, Long> {

    void upsertStatus(Status s);
}
