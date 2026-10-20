package com.nmims.lms.dto.Program;

import lombok.Data;

@Data
public class ProgramDto {
    private Long id;
    private String programName;
    private String abbr;
    private String acadYear;
    private  String active;

}
