package com.nmims.lms.utilities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class ResponseStructure<T> {

    private int statusCode; // HTTP status code (e.g., 200, 404, 500)
    private String message; // A descriptive message (e.g., "Success", "Not Found")
    private T data; // Generic type to hold the response data



}
