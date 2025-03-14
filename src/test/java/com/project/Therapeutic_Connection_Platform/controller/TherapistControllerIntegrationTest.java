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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TherapistControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllTherapists_shouldReturnAllTherapists() {
        // When
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/therapists",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Therapist>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
    }

    @Test
    void getTherapistById_withValidId_shouldReturnTherapist() {
        // When
        ResponseEntity<Therapist> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/therapists/1",
                Therapist.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
        assertEquals("Dr. Ayşe Yılmaz", response.getBody().getName());
    }

    @Test
    void getTherapistById_withInvalidId_shouldReturnNotFound() {
        // When
        ResponseEntity<Therapist> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/therapists/999",
                Therapist.class
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void searchTherapists_withSpecialization_shouldReturnMatchingTherapists() {
        // When
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/therapists/search?specialization=Anxiety",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Therapist>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Dr. Ayşe Yılmaz", response.getBody().get(0).getName());
    }

    @Test
    void searchTherapists_withLocation_shouldReturnMatchingTherapists() {
        // When
        ResponseEntity<List<Therapist>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/therapists/search?location=Istanbul",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Therapist>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Dr. Ayşe Yılmaz", response.getBody().get(0).getName());
    }
} 