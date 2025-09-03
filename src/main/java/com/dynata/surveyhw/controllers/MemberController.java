package com.dynata.surveyhw.controllers;

import com.dynata.surveyhw.dtos.PageRequest;
import com.dynata.surveyhw.dtos.csv.MemberCsvDto;
import com.dynata.surveyhw.services.CsvService;
import com.dynata.surveyhw.services.MemberService;
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
@Path("/members")
public class MemberController {

    @EJB
    private CsvService csvService;

    @EJB
    private MemberService memberService;

    @POST
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response uploadMembersCsv(@FormParam("file") InputStream file) {
        List<MemberCsvDto> memberDtos = csvService.readFromCsv(file, MemberCsvDto.class);
        return Response.ok(memberService.saveMemberDtos(memberDtos))
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    @Path("/by-survey-and-completed")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getBySurveyIdAndIsCompleted(@QueryParam("surveyId") Long surveyId,
            @BeanParam PageRequest pageable) {
        return Response.ok(memberService.getBySurveyIdAndIsCompleted(surveyId, pageable))
                .status(Response.Status.OK)
                .build();
    }

    @GET
    @Path("/by-not-participated-survey-and-active")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getByNotParticipatedInSurveyAndIsActive(
            @QueryParam("surveyId") Long surveyId,
            @BeanParam PageRequest pageable) {
        return Response.ok(memberService.getByNotParticipatedInSurveyAndIsActive(surveyId, pageable))
                .status(Response.Status.OK)
                .build();
    }
}
