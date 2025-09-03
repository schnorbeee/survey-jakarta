package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.MemberDto;
import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.csv.MemberCsvDto;
import com.dynata.surveyhw.mappers.MemberMapper;
import com.dynata.surveyhw.mappers.PageMapper;
import com.dynata.surveyhw.repositories.MemberRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class MemberService {

    @EJB
    private MemberRepository memberRepository;

    @Inject
    private MemberMapper memberMapper;

    @Inject
    private PageMapper pageMapper;

    public List<MemberDto> saveMemberDtos(List<MemberCsvDto> memberDtos) {
        return memberDtos.stream()
                .map(memberMapper::toEntity)
                .peek(memberRepository::upsertMember)
                .map(memberMapper::toDto)
                .toList();
    }

    public PageDto<MemberDto> getBySurveyIdAndIsCompleted(Long surveyId, PageRequest pageable) {
        return pageMapper.memberDtos(memberRepository.findBySurveyIdAndIsCompleted(surveyId, pageable));
    }

    public PageDto<MemberDto> getByNotParticipatedInSurveyAndIsActive(Long surveyId, PageRequest pageable) {
        return pageMapper.memberDtos(memberRepository.findByNotParticipatedSurveyAndIsActive(surveyId, pageable));
    }
}
