package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long>, MemberCustomRepository {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO member (member_id, full_name, email_address, is_active)
            VALUES (:#{#m.memberId}, :#{#m.fullName}, :#{#m.emailAddress}, :#{#m.isActive})
            ON CONFLICT (member_id)
            DO UPDATE SET full_name = EXCLUDED.full_name,
                          email_address = EXCLUDED.email_address,
                          is_active = EXCLUDED.is_active
            """, nativeQuery = true)
    void upsertMember(Member m);
}
