package com.nmims.lms.dto.Ica;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Response for bulk ICA creation — per-module status summary.
 */
@Data
@Builder
public class BulkIcaResponse {

    private int totalRequested;
    private int created;
    private int skipped;
    private int failed;
    private List<BulkIcaItemResult> results;

    @Data
    @Builder
    public static class BulkIcaItemResult {
        private String moduleId;
        private String status;   // "CREATED", "DUPLICATE", "FAILED"
        private Long icaId;      // non-null when CREATED
        private String message;
    }
}
