package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.jpaRepos.TherapistRepository;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.modelEnums.VerificationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/verification")
@CrossOrigin(origins = "https://therapeutic-connection-platform-1.onrender.com")
public class VerificationAdminController {


        @Autowired
        private TherapistRepository therapistRepo;

        @GetMapping("/pending")
        public List<Therapist> pending() {
            return therapistRepo.findByVerificationState(VerificationState.PENDING);
        }

        @PutMapping("/{id}/approve")
        public ResponseEntity<?> approve(@PathVariable Long id) {
            Therapist t = therapistRepo.findById(id).orElseThrow();
            t.setVerificationState(VerificationState.VERIFIED);
            therapistRepo.save(t);
            return ResponseEntity.ok().build();
        }
    }


