package com.dynata.surveyhw.controllers;

import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.csv.SurveyCsvDto;
import com.dynata.surveyhw.services.CsvService;
import com.dynata.surveyhw.services.SurveyService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

@RequestScoped
@Path("/surveys")
public class SurveyController {

    @EJB
    private CsvService csvService;

    @EJB
    private SurveyService surveyService;

    @POST
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response uploadSurveysCsv(@FormParam("file") InputStream file) {
        List<SurveyCsvDto> surveyDtoList = csvService.readFromCsv(file, SurveyCsvDto.class);
        return Response.ok(surveyService.saveSurveyDtos(surveyDtoList))
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    @Path("/by-member-id-and-completed")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getByMemberIdAndIsCompleted(@QueryParam("memberId") Long memberId,
            @BeanParam PageRequest pageable) {
        return Response.ok(surveyService.getByMemberIdAndIsCompleted(memberId, pageable))
                .status(Response.Status.OK)
                .build();
    }

    @GET
    @Path("/by-member-id-completion-points")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getSurveyCompletionPointsByMemberId(@QueryParam("memberId") Long memberId) {
        return Response.ok(surveyService.getSurveyCompletionPointsByMemberId(memberId))
                .status(Response.Status.OK)
                .build();
    }

    @GET
    @Path("/all-statistic")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getAllStatisticSurveys(@BeanParam PageRequest pageable) {
        return Response.ok(surveyService.getAllStatisticSurveys(pageable))
                .status(Response.Status.OK)
                .build();
    }
}
