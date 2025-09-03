package com.dynata.surveyhw.repositories;

import com.dynata.surveyhw.entities.Member;

public interface MemberRepository extends GenericRepository<Member, Long>, MemberCustomRepository {
    
    void upsertMember(Member m);
}
