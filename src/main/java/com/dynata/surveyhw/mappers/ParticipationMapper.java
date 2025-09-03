package com.dynata.surveyhw.mappers;

import com.dynata.surveyhw.dtos.ParticipationDto;
import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.entities.Member;
import com.dynata.surveyhw.entities.Participation;
import com.dynata.surveyhw.entities.Status;
import com.dynata.surveyhw.entities.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface ParticipationMapper {

    @Mapping(target = "memberId", source = "entity.member.memberId")
    @Mapping(target = "surveyId", source = "entity.survey.surveyId")
    @Mapping(target = "statusId", source = "entity.status.statusId")
    ParticipationDto toDto(Participation entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "member", source = "memberId")
    @Mapping(target = "survey", source = "surveyId")
    @Mapping(target = "status", source = "statusId")
    Participation toEntity(ParticipationCsvDto dto);

    default Member mapMember(Long id) {
        if (id == null) {
            return null;
        }
        Member m = new Member();
        m.setMemberId(id);
        return m;
    }

    default Survey mapSurvey(Long id) {
        if (id == null) {
            return null;
        }
        Survey s = new Survey();
        s.setSurveyId(id);
        return s;
    }

    default Status mapStatus(Long id) {
        if (id == null) {
            return null;
        }
        Status st = new Status();
        st.setStatusId(id);
        return st;
    }
}
