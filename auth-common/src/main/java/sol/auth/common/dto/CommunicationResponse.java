package sol.auth.common.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationResponse implements Serializable {

    private boolean success;
    private String messageId;
    private String message;
    private Instant timestamp;

    public static CommunicationResponse ok(String messageId, String message) {
        return CommunicationResponse.builder()
                .success(true)
                .messageId(messageId)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }

    public static CommunicationResponse failure(String message) {
        return CommunicationResponse.builder()
                .success(false)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
