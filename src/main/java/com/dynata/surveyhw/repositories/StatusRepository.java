package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO status (status_id, name)
            VALUES (:#{#s.statusId}, :#{#s.name})
            ON CONFLICT (status_id)
            DO UPDATE SET name = EXCLUDED.name
            """, nativeQuery = true)
    void upsertStatus(Status s);
}
