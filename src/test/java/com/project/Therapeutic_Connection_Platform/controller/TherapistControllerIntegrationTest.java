package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TherapistControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/therapists";
    }

    @Test
    void getAllTherapists_shouldReturnAllTherapists() {
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
            getBaseUrl(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Therapist>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTherapistById_withValidId_shouldReturnTherapist() {
        ResponseEntity<Therapist> response = restTemplate.getForEntity(
            getBaseUrl() + "/id/1",
            Therapist.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getTherapistById_withInvalidId_shouldReturnNotFound() {
        ResponseEntity<Therapist> response = restTemplate.getForEntity(
            getBaseUrl() + "/id/999",
            Therapist.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchTherapists_withSpecialization_shouldReturnMatchingTherapists() {
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
            getBaseUrl() + "/search?specialization=Anxiety",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Therapist>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void searchTherapists_withLocation_shouldReturnMatchingTherapists() {
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
            getBaseUrl() + "/search?location=Istanbul",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Therapist>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
} 