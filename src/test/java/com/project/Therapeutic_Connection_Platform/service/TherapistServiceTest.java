package com.project.Therapeutic_Connection_Platform.service;

import com.project.Therapeutic_Connection_Platform.jpaRepos.TherapistRepository;
import com.project.Therapeutic_Connection_Platform.model.Language;
import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TherapistServiceTest {

    private TherapistService therapistService;

    @Mock
    private TherapistRepository therapistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        therapistService = new TherapistService(therapistRepository);
    }

    @Test
    void getAllTherapists_shouldReturnAllTherapists() {
        // Given
        List<Therapist> therapists = createTestTherapists();
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.getAllTherapists();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(therapistRepository, times(1)).findAll();
    }

    @Test
    void getTherapistById_withValidId_shouldReturnTherapist() {
        // Given
        Therapist therapist = createTestTherapist(1L);
        when(therapistRepository.findById(1L)).thenReturn(Optional.of(therapist));

        // When
        Therapist result = therapistService.getTherapistById("1");

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(therapistRepository, times(1)).findById(1L);
    }

    @Test
    void getTherapistById_withInvalidId_shouldReturnNull() {
        // Given
        when(therapistRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Therapist result = therapistService.getTherapistById("999");

        // Then
        assertNull(result);
        verify(therapistRepository, times(1)).findById(999L);
    }

    @Test
    void searchTherapists_BySpecialization_ShouldReturnMatchingTherapists() {
        // Given
        List<Therapist> therapists = Arrays.asList(
            createTestTherapist(1L),
            createTestTherapist(2L)
        );
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.searchTherapists("Anxiety", null, null);

        // Then
        assertEquals(1, result.size());
        assertEquals("Anxiety", result.get(0).getSpecializations().get(0));
    }

    @Test
    void searchTherapists_ByLocation_ShouldReturnMatchingTherapists() {
        // Given
        List<Therapist> therapists = Arrays.asList(
            createTestTherapist(1L),
            createTestTherapist(2L)
        );
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.searchTherapists(null, "Istanbul", null);

        // Then
        assertEquals(2, result.size()); // Both therapists are in Istanbul
        assertEquals("Istanbul", result.get(0).getLocation().getName());
        assertEquals("Istanbul", result.get(1).getLocation().getName());
    }

    @Test
    void searchTherapists_ByLanguage_ShouldReturnMatchingTherapists() {
        // Given
        List<Therapist> therapists = Arrays.asList(
            createTestTherapist(1L),
            createTestTherapist(2L)
        );
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.searchTherapists(null, null, Arrays.asList("English"));

        // Then
        assertEquals(1, result.size());
        assertEquals("English", result.get(0).getLanguages().get(0).getLangName());
    }

    @Test
    void searchTherapists_withMultipleParameters_shouldReturnMatchingTherapists() {
        // Given
        List<Therapist> therapists = createTestTherapists();
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.searchTherapists("Depression", "Istanbul", Arrays.asList("Turkish", "English"));

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Depression", result.get(0).getSpecializations().get(0));
        assertEquals("Istanbul", result.get(0).getLocation().getName());
    }

    @Test
    void searchTherapists_withNoMatches_shouldReturnEmptyList() {
        // Given
        List<Therapist> therapists = createTestTherapists();
        when(therapistRepository.findAll()).thenReturn(therapists);

        // When
        List<Therapist> result = therapistService.searchTherapists("Nonexistent", null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private List<Therapist> createTestTherapists() {
        Therapist therapist1 = createTestTherapist(1L);
        Therapist therapist2 = createTestTherapist(2L);
        return Arrays.asList(therapist1, therapist2);
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
        location.setName("Istanbul");
        return location;
    }

    private Language createTestLanguage(Long id) {
        Language language = new Language();
        language.setLangName(id == 1L ? "German" : "English");
        return language;
    }
} 