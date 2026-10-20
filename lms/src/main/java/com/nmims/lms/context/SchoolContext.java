package com.nmims.lms.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Immutable context object carrying school/campus identity.
 * Resolved from authenticated user and threaded through service calls.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolContext {
    private String campusCode;
    private String school;
}
