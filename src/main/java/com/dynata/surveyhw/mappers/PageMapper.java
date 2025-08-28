package com.dynata.surveyhw.mappers;

import com.dynata.surveyhw.dtos.MemberDto;
import com.dynata.surveyhw.dtos.PageDto;
import com.dynata.surveyhw.dtos.SurveyDto;
import com.dynata.surveyhw.entities.Member;
import com.dynata.surveyhw.entities.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MemberMapper.class, SurveyMapper.class, StatusMapper.class, ParticipationMapper.class
})
public interface PageMapper {

    @Mapping(target = "content", source = "members.content")
    @Mapping(target = "pageNumber", source = "members.pageNumber")
    @Mapping(target = "pageSize", source = "members.pageSize")
    @Mapping(target = "totalElements", source = "members.totalElements")
    @Mapping(target = "numberOfElements", source = "members.numberOfElements")
    PageDto<MemberDto> memberDtos(PageDto<Member> members);

    @Mapping(target = "content", source = "surveys.content")
    @Mapping(target = "pageNumber", source = "surveys.pageNumber")
    @Mapping(target = "pageSize", source = "surveys.pageSize")
    @Mapping(target = "totalElements", source = "surveys.totalElements")
    @Mapping(target = "numberOfElements", source = "surveys.numberOfElements")
    PageDto<SurveyDto> surveyDtos(PageDto<Survey> surveys);
}
