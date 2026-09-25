package sol.auth.client.sdk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import sol.auth.client.config.AuthClientProperties;
import sol.auth.client.tenant.TenantContext;
import sol.auth.common.dto.CommunicationResponse;
import sol.auth.common.dto.EmailRequest;
import sol.auth.common.dto.SmsRequest;

public class UserManagementClient {

    private static final Logger log = LoggerFactory.getLogger(UserManagementClient.class);

    private final AuthClientProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public UserManagementClient(AuthClientProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public CommunicationResponse sendEmail(EmailRequest request) {
        String url = properties.getServerUrl() + "/api/v1/communications/email";
        return postJson(url, request, CommunicationResponse.class);
    }

    public CommunicationResponse sendSms(SmsRequest request) {
        String url = properties.getServerUrl() + "/api/v1/communications/sms";
        return postJson(url, request, CommunicationResponse.class);
    }

    private <T> T postJson(String url, Object payload, Class<T> responseType) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(json));

            if (TenantContext.hasTenant()) {
                reqBuilder.header("X-Tenant-Id", String.valueOf(TenantContext.getTenantId()));
            }

            HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), responseType);
            } else {
                log.warn("UserManagement call failed with status: {}, body: {}", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception ex) {
            log.error("Failed to execute request to UserManagement server: {}", ex.getMessage());
            return null;
        }
    }
}
