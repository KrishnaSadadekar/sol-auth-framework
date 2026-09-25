package sol.auth.common.dto;

import java.io.Serializable;
import java.util.Map;

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
public class EmailRequest implements Serializable {

    private String to;
    private String subject;
    private String body;
    private String templateName;
    private Map<String, Object> templateVariables;
    private boolean html;
}
