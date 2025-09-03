package com.dynata.surveyhw.utils;

import com.dynata.surveyhw.repositories.MemberRepository;
import com.dynata.surveyhw.repositories.ParticipationRepository;
import com.dynata.surveyhw.repositories.StatusRepository;
import com.dynata.surveyhw.repositories.SurveyRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/test-utils")
@Stateless
public class TestUtilsResource {

    @EJB
    private MemberRepository memberRepository;

    @EJB
    private StatusRepository statusRepository;

    @EJB
    private SurveyRepository surveyRepository;

    @EJB
    private ParticipationRepository participationRepository;

    @EJB
    private InitFullState initFullState;

    @POST
    @Path("/init")
    public void init() {
        initFullState.initAllCsv(false);
    }

    @POST
    @Path("/init-except")
    public void initExcept() {
        initFullState.initAllCsv(true);
    }

    @POST
    @Path("/cleanup")
    public void cleanup() {
        initFullState.deleteFullDatabase();
    }

    @POST
    @Path("/findAllMember")
    public int findAllMember() {
        return memberRepository.findAll().size();
    }

    @POST
    @Path("/findAllStatus")
    public int findAllStatus() {
        return statusRepository.findAll().size();
    }

    @POST
    @Path("/findAllSurvey")
    public int findAllSurvey() {
        return surveyRepository.findAll().size();
    }

    @POST
    @Path("/findAllParticipation")
    public int findAllParticipation() {
        return participationRepository.findAll().size();
    }

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok("pong").build();
    }
}

