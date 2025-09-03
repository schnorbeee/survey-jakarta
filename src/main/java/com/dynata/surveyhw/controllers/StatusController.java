package com.dynata.surveyhw.controllers;

import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.services.CsvService;
import com.dynata.surveyhw.services.StatusService;
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
@Path("/statuses")
public class StatusController {

    @EJB
    private CsvService csvService;

    @EJB
    private StatusService statusService;

    @POST
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response uploadStatusesCsv(@FormParam("file") InputStream file) {
        List<StatusCsvDto> statusDtos = csvService.readFromCsv(file, StatusCsvDto.class);
        return Response.ok(statusService.saveStatusDtos(statusDtos))
                .status(Response.Status.CREATED)
                .build();
    }
}
