package com.dynata.surveyhw.utils;

import com.dynata.surveyhw.dtos.csv.MemberCsvDto;
import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.dtos.csv.SurveyCsvDto;
import com.dynata.surveyhw.entities.Member;
import com.dynata.surveyhw.entities.Participation;
import com.dynata.surveyhw.entities.Status;
import com.dynata.surveyhw.entities.Survey;
import com.dynata.surveyhw.mappers.MemberMapper;
import com.dynata.surveyhw.mappers.ParticipationMapper;
import com.dynata.surveyhw.mappers.StatusMapper;
import com.dynata.surveyhw.mappers.SurveyMapper;
import com.dynata.surveyhw.repositories.MemberRepository;
import com.dynata.surveyhw.repositories.ParticipationRepository;
import com.dynata.surveyhw.repositories.StatusRepository;
import com.dynata.surveyhw.repositories.SurveyRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.List;

@Stateless
public class InitFullState {

    @EJB
    private MemberRepository memberRepository;

    @EJB
    private StatusRepository statusRepository;

    @EJB
    private SurveyRepository surveyRepository;

    @EJB
    private ParticipationRepository participationRepository;

    @Inject
    private StatusMapper statusMapper;

    @Inject
    private MemberMapper memberMapper;

    @Inject
    private SurveyMapper surveyMapper;

    @Inject
    private ParticipationMapper participationMapper;

    public void deleteFullDatabase() {
        participationRepository.deleteAll();
        surveyRepository.deleteAll();
        statusRepository.deleteAll();
        memberRepository.deleteAll();
    }

    public void initAllCsv(boolean exceptParticipation) {
        List<Status> statuses = readFromCsv(getClass().getClassLoader().getResourceAsStream("testfiles/Statuses.csv"),
                StatusCsvDto.class).stream().map(statusMapper::toEntity).toList();
        statusRepository.saveAll(statuses);

        List<Member> members = readFromCsv(getClass().getClassLoader().getResourceAsStream("testfiles/Members.csv"),
                MemberCsvDto.class).stream().map(memberMapper::toEntity).toList();
        memberRepository.saveAll(members);

        List<Survey> surveys = readFromCsv(getClass().getClassLoader().getResourceAsStream("testfiles/Surveys.csv"),
                SurveyCsvDto.class).stream().map(surveyMapper::toEntity).toList();
        surveyRepository.saveAll(surveys);

        if (!exceptParticipation) {
            List<Participation> participations = readFromCsv(
                    getClass().getClassLoader().getResourceAsStream("testfiles/Participations.csv"),
                    ParticipationCsvDto.class).stream().map(participationMapper::toEntity).toList();
            participationRepository.saveAll(participations);
        }
    }

    private <T> List<T> readFromCsv(InputStream file, Class<T> clazz) {
        try (InputStream inputStream = file) {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<T> iterator = mapper.readerFor(clazz)
                    .with(schema)
                    .readValues(inputStream);
            return iterator.readAll();
        } catch (Exception e) {
            throw new RuntimeException("Error at reading " + clazz + " CSV file: " + e.getMessage(), e);
        }
    }
}
