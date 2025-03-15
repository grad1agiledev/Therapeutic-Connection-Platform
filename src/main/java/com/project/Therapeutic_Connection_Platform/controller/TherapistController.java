package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@CrossOrigin(origins = "*") // Allow access from all sources during development
public class TherapistController {

    private final TherapistService therapistService;

    @Autowired
    public TherapistController(TherapistService therapistService) {
        this.therapistService = therapistService;
    }

    @GetMapping
    public ResponseEntity<List<Therapist>> getAllTherapists() {
        return ResponseEntity.ok(therapistService.getAllTherapists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Therapist> getTherapistById(@PathVariable String id) {
        Therapist therapist = therapistService.getTherapistById(id);
        if (therapist != null) {
            return ResponseEntity.ok(therapist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Therapist>> searchTherapists(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) List<String> languages) {
        return ResponseEntity.ok(therapistService.searchTherapists(specialization, location, languages));
    }
} 