package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.service.TherapistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TherapistControllerTest {

    @Mock
    private TherapistService therapistService;

    @InjectMocks
    private TherapistController therapistController;

    private Therapist therapist1;
    private Therapist therapist2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        therapist1 = new Therapist(
                "1",
                "Dr. Ayşe Yılmaz",
                "Anxiety, Depression",
                "Istanbul",
                Arrays.asList("Turkish", "English"),
                4.8,
                500.0,
                "https://randomuser.me/api/portraits/women/1.jpg",
                "Clinical psychologist with 10 years of experience. Specialized in anxiety and depression treatment.",
                true
        );

        therapist2 = new Therapist(
                "2",
                "Dr. Mehmet Kaya",
                "Family Therapy, Couples Therapy",
                "Ankara",
                Collections.singletonList("Turkish"),
                4.5,
                450.0,
                "https://randomuser.me/api/portraits/men/2.jpg",
                "Expert psychologist with 15 years of experience in family and couples therapy.",
                true
        );
    }

    @Test
    void getAllTherapists_shouldReturnAllTherapists() {
        // Given
        List<Therapist> therapists = Arrays.asList(therapist1, therapist2);
        when(therapistService.getAllTherapists()).thenReturn(therapists);

        // When
        ResponseEntity<List<Therapist>> response = therapistController.getAllTherapists();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(therapistService, times(1)).getAllTherapists();
    }

    @Test
    void getTherapistById_withValidId_shouldReturnTherapist() {
        // Given
        when(therapistService.getTherapistById("1")).thenReturn(therapist1);

        // When
        ResponseEntity<Therapist> response = therapistController.getTherapistById("1");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getId());
        verify(therapistService, times(1)).getTherapistById("1");
    }

    @Test
    void getTherapistById_withInvalidId_shouldReturnNotFound() {
        // Given
        when(therapistService.getTherapistById("999")).thenReturn(null);

        // When
        ResponseEntity<Therapist> response = therapistController.getTherapistById("999");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(therapistService, times(1)).getTherapistById("999");
    }

    @Test
    void searchTherapists_shouldReturnMatchingTherapists() {
        // Given
        String specialization = "Anxiety";
        String location = "Istanbul";
        List<String> languages = Arrays.asList("Turkish", "English");
        
        when(therapistService.searchTherapists(specialization, location, languages))
                .thenReturn(Collections.singletonList(therapist1));

        // When
        ResponseEntity<List<Therapist>> response = therapistController.searchTherapists(specialization, location, languages);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Dr. Ayşe Yılmaz", response.getBody().get(0).getName());
        verify(therapistService, times(1)).searchTherapists(specialization, location, languages);
    }
} 