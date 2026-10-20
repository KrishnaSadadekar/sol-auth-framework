package com.nmims.lms;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import sol.auth.client.config.AuthClientProperties;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LmsAuthenticationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthClientProperties authProperties;

    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        this.signingKey = Keys.hmacShaKeyFor(authProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    private String createValidToken(String username, Long userId, Long tenantId, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(authProperties.getIssuer())
                .subject(username)
                .claim("uid", userId)
                .claim("tid", tenantId)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(signingKey)
                .compact();
    }

    @Test
    @DisplayName("Public endpoints should be accessible without any token")
    void testPublicEndpointPermitAll() throws Exception {
        mockMvc.perform(get("/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("lms")));
    }

    @Test
    @DisplayName("Protected endpoint should return 401 Unauthorized when token is missing")
    void testProtectedEndpointWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/v1/user/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("Unauthorized")));
    }

    @Test
    @DisplayName("Protected endpoint should return 401 Unauthorized when token is invalid")
    void testProtectedEndpointWithInvalidTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/v1/user/me")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("Protected endpoint should return 200 OK and principal when valid JWT token is provided")
    void testProtectedEndpointWithValidJwtToken() throws Exception {
        String token = createValidToken("prof_sharma", 42L, 7L, List.of("FACULTY", "ADMIN"));

        mockMvc.perform(get("/api/v1/user/me")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.username", is("prof_sharma")))
                .andExpect(jsonPath("$.data.userId", is(42)))
                .andExpect(jsonPath("$.data.tenantId", is(7)))
                .andExpect(jsonPath("$.data.roles", hasItems("FACULTY", "ADMIN")));
    }

    @Test
    @DisplayName("LMS Academic endpoints should be protected and succeed with valid JWT token")
    void testAcademicEndpointProtectedWithToken() throws Exception {
        String token = createValidToken("student_rahul", 108L, 7L, List.of("STUDENT"));

        // Unauthenticated access to /campuses should return 401
        mockMvc.perform(get("/campuses"))
                .andExpect(status().isUnauthorized());

        // Authenticated access with valid token should succeed
        mockMvc.perform(get("/campuses")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
