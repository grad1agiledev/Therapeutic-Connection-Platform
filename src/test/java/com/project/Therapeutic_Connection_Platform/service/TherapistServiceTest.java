package com.project.Therapeutic_Connection_Platform.service;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TherapistServiceTest {

    private TherapistService therapistService;

    @BeforeEach
    void setUp() {
      //  therapistService = new TherapistService();
    }

    @Test
    void getAllTherapists_shouldReturnAllTherapists() {
        // When
        List<Therapist> result = therapistService.getAllTherapists();

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    void getTherapistById_withValidId_shouldReturnTherapist() {
        // When
        Therapist result = therapistService.getTherapistById("1");

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        //assertEquals("Dr. Ayşe Yılmaz", result.getName());
    }

    @Test
    void getTherapistById_withInvalidId_shouldReturnNull() {
        // When
        Therapist result = therapistService.getTherapistById("999");

        // Then
        assertNull(result);
    }

    @Test
    void searchTherapists_withSpecialization_shouldReturnMatchingTherapists() {
        // When
        List<Therapist> result = therapistService.searchTherapists("Anxiety", null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        //assertEquals("Dr. Ayşe Yılmaz", result.get(0).getName());
    }

    @Test
    void searchTherapists_withLocation_shouldReturnMatchingTherapists() {
        // When
        List<Therapist> result = therapistService.searchTherapists(null, "Istanbul", null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        //assertEquals("Dr. Ayşe Yılmaz", result.get(0).getName());
    }

    @Test
    void searchTherapists_withLanguage_shouldReturnMatchingTherapists() {
        // When
        List<Therapist> result = therapistService.searchTherapists(null, null, Collections.singletonList("German"));

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        //assertEquals("Dr. Zeynep Demir", result.get(0).getName());
    }

    @Test
    void searchTherapists_withMultipleParameters_shouldReturnMatchingTherapists() {
        // When
        List<Therapist> result = therapistService.searchTherapists("Depression", "Istanbul", Arrays.asList("Turkish", "English"));

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        //assertEquals("Dr. Ayşe Yılmaz", result.get(0).getName());
    }

    @Test
    void searchTherapists_withNoMatches_shouldReturnEmptyList() {
        // When
        List<Therapist> result = therapistService.searchTherapists("Nonexistent", null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
} 