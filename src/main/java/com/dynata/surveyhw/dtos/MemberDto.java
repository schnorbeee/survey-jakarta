package com.dynata.surveyhw.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDto {

    private Long memberId;

    private String fullName;

    private String emailAddress;

    private Boolean isActive;
}
