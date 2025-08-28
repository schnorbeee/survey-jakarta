package com.dynata.surveyhw.services;

import com.dynata.surveyhw.dtos.MemberDto;
import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.csv.MemberCsvDto;
import com.dynata.surveyhw.mappers.MemberMapper;
import com.dynata.surveyhw.mappers.PageMapper;
import com.dynata.surveyhw.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    private final PageMapper pageMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper, PageMapper pageMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.pageMapper = pageMapper;
    }

    public List<MemberDto> saveMemberDtos(List<MemberCsvDto> memberDtos) {
        return memberDtos.stream()
                .map(memberMapper::toEntity)
                .peek(memberRepository::upsertMember)
                .map(memberMapper::toDto)
                .toList();
    }

    public PageDto<MemberDto> getBySurveyIdAndIsCompleted(Long surveyId, Pageable pageable) {
        return pageMapper.memberDtos(memberRepository.findBySurveyIdAndIsCompleted(surveyId, pageable));
    }

    public PageDto<MemberDto> getByNotParticipatedInSurveyAndIsActive(Long surveyId, Pageable pageable) {
        return pageMapper.memberDtos(memberRepository.findByNotParticipatedSurveyAndIsActive(surveyId, pageable));
    }
}
