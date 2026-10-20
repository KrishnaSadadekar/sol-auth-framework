package com.nmims.lms.Exceptions;

import com.nmims.lms.utilities.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice 
public class GlobalException {

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGenericException(Exception ex) {
        ResponseStructure<String> response=  ResponseStructure.<String>builder().statusCode(HttpStatus.NOT_FOUND.value()).message("An unexpected error occurred").statusCode(404).build();
        response.setMessage("An unexpected error occurred");
        response.setData(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

   
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
     ResponseStructure<String> response=  ResponseStructure.<String>builder().statusCode(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).statusCode(404).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // Collect field errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        // Build response
        ResponseStructure<Map<String, String>> response = ResponseStructure.<Map<String, String>>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .data(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseStructure<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseStructure<String> response = ResponseStructure.<String>builder().statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Invalid argument")
        .data(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ResponseStructure<String>> handleCustomValidationException(CustomValidationException ex) {
        ResponseStructure<String> response = ResponseStructure.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }




}
