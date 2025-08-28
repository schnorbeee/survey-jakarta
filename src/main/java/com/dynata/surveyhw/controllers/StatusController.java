package com.dynata.surveyhw.controllers;

import com.dynata.surveyhw.dtos.StatusDto;
import com.dynata.surveyhw.dtos.csv.StatusCsvDto;
import com.dynata.surveyhw.handlers.responses.ExceptionResponse;
import com.dynata.surveyhw.services.CsvService;
import com.dynata.surveyhw.services.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    private final CsvService csvService;

    private final StatusService statusService;

    @Autowired(required = false)
    public StatusController(CsvService csvService, StatusService statusService) {
        this.csvService = csvService;
        this.statusService = statusService;
    }

    @Operation(summary = "Save statuses from csv file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = StatusDto.class)))),
            @ApiResponse(responseCode = "400", description = "Runtime error: HttpStatus.BAD_REQUEST",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Fatal error: HttpStatus.INTERNAL_SERVER_ERROR",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatusDto>> uploadStatusesCsv(@RequestParam("file") MultipartFile file) {
        List<StatusCsvDto> statusDtos = csvService.readFromCsv(file, StatusCsvDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.saveStatusDtos(statusDtos));
    }
}
