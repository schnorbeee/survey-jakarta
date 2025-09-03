package com.dynata.surveyhw.controllers;

import com.dynata.surveyhw.dtos.csv.ParticipationCsvDto;
import com.dynata.surveyhw.services.CsvService;
import com.dynata.surveyhw.services.ParticipationService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

@RequestScoped
@Path("/participations")
public class ParticipationController {

    @EJB
    private CsvService csvService;

    @EJB
    private ParticipationService participationService;

    @POST
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response uploadParticipationsCsv(@FormParam("file") InputStream file) {
        List<ParticipationCsvDto> participationDtos = csvService.readFromCsv(file, ParticipationCsvDto.class);
        return Response.ok(participationService.saveParticipationDtos(participationDtos))
                .status(Response.Status.CREATED)
                .build();
    }
}
