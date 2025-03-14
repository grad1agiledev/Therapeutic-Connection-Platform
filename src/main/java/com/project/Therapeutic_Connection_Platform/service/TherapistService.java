package com.project.Therapeutic_Connection_Platform.service;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TherapistService {

    // In a real application, this data would come from a database
    private final List<Therapist> therapists = new ArrayList<>();

    public TherapistService() {
        // Adding sample data
        initSampleData();
    }

    private void initSampleData() {
        therapists.add(new Therapist(
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
        ));

        therapists.add(new Therapist(
                "2",
                "Dr. Mehmet Kaya",
                "Family Therapy, Couples Therapy",
                "Ankara",
                Arrays.asList("Turkish"),
                4.5,
                450.0,
                "https://randomuser.me/api/portraits/men/2.jpg",
                "Expert psychologist with 15 years of experience in family and couples therapy.",
                true
        ));

        therapists.add(new Therapist(
                "3",
                "Dr. Zeynep Demir",
                "Trauma, EMDR",
                "Izmir",
                Arrays.asList("Turkish", "German"),
                4.9,
                550.0,
                "https://randomuser.me/api/portraits/women/3.jpg",
                "Clinical psychologist specialized in trauma treatment and EMDR.",
                true
        ));

        therapists.add(new Therapist(
                "4",
                "Dr. Ali Can",
                "Child and Adolescent Psychology",
                "Bursa",
                Arrays.asList("Turkish", "English"),
                4.7,
                480.0,
                "https://randomuser.me/api/portraits/men/4.jpg",
                "Expert psychologist with 8 years of experience in child and adolescent psychology.",
                true
        ));

        therapists.add(new Therapist(
                "5",
                "Dr. Selin Yıldız",
                "Cognitive Behavioral Therapy",
                "Antalya",
                Arrays.asList("Turkish", "French"),
                4.6,
                520.0,
                "https://randomuser.me/api/portraits/women/5.jpg",
                "Clinical psychologist specialized in cognitive behavioral therapy.",
                true
        ));
    }

    public List<Therapist> getAllTherapists() {
        return therapists;
    }

    public Therapist getTherapistById(String id) {
        return therapists.stream()
                .filter(therapist -> therapist.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Therapist> searchTherapists(String specialization, String location, List<String> languages) {
        return therapists.stream()
                .filter(therapist -> specialization == null || therapist.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
                .filter(therapist -> location == null || therapist.getLocation().equalsIgnoreCase(location))
                .filter(therapist -> languages == null || languages.isEmpty() || 
                        therapist.getLanguages().stream().anyMatch(languages::contains))
                .collect(Collectors.toList());
    }
} 