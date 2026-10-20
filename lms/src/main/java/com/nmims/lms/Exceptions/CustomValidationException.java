package com.nmims.lms.Exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class CustomValidationException extends RuntimeException{

    private final String message;
    private final HttpStatus status;



}
