package com.dynata.surveyhw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusDto {

    private Long statusId;

    private String name;
}
