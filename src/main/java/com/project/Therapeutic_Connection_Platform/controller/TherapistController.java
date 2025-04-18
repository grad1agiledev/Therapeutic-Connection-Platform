package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.TherapistUpdateRequest;
import com.project.Therapeutic_Connection_Platform.dto.VerificationRequest;
import com.project.Therapeutic_Connection_Platform.jpaRepos.LanguageRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.LocationRepository;
import com.project.Therapeutic_Connection_Platform.model.Language;
import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
import com.project.Therapeutic_Connection_Platform.modelEnums.VerificationState;
import com.project.Therapeutic_Connection_Platform.service.TherapistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/therapists")
@CrossOrigin(origins = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        })
public class TherapistController {

    private final TherapistService therapistService;
    private final LocationRepository locationRepo;

    @Autowired private LanguageRepository languageRepo;
    @Autowired
    public TherapistController(TherapistService therapistService,LocationRepository locationRepository) {
        this.therapistService = therapistService;
        this.locationRepo = locationRepository;
    }

    @GetMapping
    public ResponseEntity<List<Therapist>> getAllTherapists() {
        return ResponseEntity.ok(therapistService.getAllTherapists());
    }


    @GetMapping("/{uid}")
    public ResponseEntity<?> getTherapistByUid(@PathVariable String uid) {
        Therapist therapist = therapistService.getTherapistByFirebaseUid(uid);
        if (therapist == null) {
            return ResponseEntity.badRequest().body("User not found or user is not a therapist.");
        }
        return ResponseEntity.ok(therapist);
    }

    /*
    Updating the all therapists info if accessed a request
     */
    @PutMapping("/{uid}")
    public ResponseEntity<?> updateTherapist(
            @PathVariable String uid,
            @RequestBody TherapistUpdateRequest req) {

        Therapist therapist = therapistService.getTherapistByFirebaseUid(uid);
        if (therapist == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Therapist not found"
            );
        }

        therapist.setSpecialization(req.specialization);
        therapist.setBio(req.bio);
        therapist.setSessionCost(req.sessionCost);
       // therapist.setLanguages(req.languages);
        Location loc = locationRepo.findById(req.locationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid locationId"
                ));
        therapist.setLocation(loc);


        List<Language> langs = languageRepo.findAllById(req.languageIds);
        therapist.setLanguages(langs);
        therapist.setProfilePicture(req.profilePicture);
        therapistService.saveTherapist(therapist);
        return ResponseEntity.ok().build();
    }


    /*
    recording the uploading photo to the DB
     */
    @PostMapping(path = "/{uid}/uploadPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,String>> uploadPhoto(
            @PathVariable String uid,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        Therapist t = therapistService.getTherapistByFirebaseUid(uid);
        if (t == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // absolute path creating for the db record
        String url = "http://localhost:8080/uploads/" + filename;


        t.setProfilePicture(url);
        therapistService.saveTherapist(t);

        return ResponseEntity.ok(Map.of("url", url));
    }

    /*
    setting verification infos for therapist
     */
    @PostMapping("/{uid}/verify")
    public ResponseEntity<?> askForVerification(
            @PathVariable String uid,
            @RequestBody VerificationRequest dto) {

        Therapist t = therapistService.getTherapistByFirebaseUid(uid);
        if (t == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        t.setLicenceDocument(dto.licenceDocument);
        t.setVerificationState(VerificationState.PENDING);

        therapistService.saveTherapist(t);
        return ResponseEntity.ok().build();
    }

    /*
    uploading licenses endpoint
     */
    @PostMapping(path = "/{uid}/uploadLicence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String,String>> uploadLicence(
            @PathVariable String uid,
            @RequestPart("file") MultipartFile file) throws IOException {

        Therapist t = therapistService.getTherapistByFirebaseUid(uid);
        if (t == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path uploadDir  = Paths.get("uploads/licences");
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        String url = "http://localhost:8080/uploads/licences/" + filename;
        return ResponseEntity.ok(Map.of("url", url));
    }


}