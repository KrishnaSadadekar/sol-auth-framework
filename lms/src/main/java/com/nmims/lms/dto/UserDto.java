package com.nmims.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String enrollmentYear;
    private String enrollmentMonth;
    private String validityEndYear;
    private String validityEndMonth;
    private String fatherName;
    private String motherName;
    private Integer programId;
    private String address;
    private String profilePicture;
    private String status;
    private String role;
    
}
