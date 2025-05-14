package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.jpaRepos.LocationRepository;
import com.project.Therapeutic_Connection_Platform.model.Language;
import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
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

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private TherapistController therapistController;

    private Therapist therapist1;
    private Therapist therapist2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        therapist1 = createTestTherapist(1L);
        therapist2 = createTestTherapist(2L);
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
        assertEquals(1L, response.getBody().getId());
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
        assertEquals("Anxiety", response.getBody().get(0).getSpecializations().get(0));
        verify(therapistService, times(1)).searchTherapists(specialization, location, languages);
    }

    @Test
    void searchTherapists_BySpecialization_ShouldReturnMatchingTherapists() {
        // Given
        List<Therapist> therapists = Arrays.asList(
            createTestTherapist(1L),
            createTestTherapist(2L)
        );
        when(therapistService.searchTherapists("Anxiety", null, null)).thenReturn(therapists);

        // When
        ResponseEntity<List<Therapist>> response = therapistController.searchTherapists("Anxiety", null, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Anxiety", response.getBody().get(0).getSpecializations().get(0));
    }

    private Therapist createTestTherapist(Long id) {
        Therapist therapist = new Therapist();
        therapist.setId(id);
        therapist.setUser(createTestUser(id));
        therapist.setSpecializations(Arrays.asList(id == 1L ? "Anxiety" : "Depression"));
        therapist.setLocation(createTestLocation(id));
        therapist.setLanguages(Arrays.asList(createTestLanguage(id)));
        return therapist;
    }

    private User createTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setFullName("Test User " + id);
        return user;
    }

    private Location createTestLocation(Long id) {
        Location location = new Location();
        location.setId(id);
        location.setName("Istanbul");
        return location;
    }

    private Language createTestLanguage(Long id) {
        Language language = new Language();
        language.setId(id);
        language.setLangName(id == 1L ? "English" : "German");
        return language;
    }
} 