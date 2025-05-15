package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.UserRegisterRequest;
import com.project.Therapeutic_Connection_Platform.jpaRepos.LocationRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.TherapistRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.UserRepository;
import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.Therapist;
import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RegisterController {

    @Autowired private UserRepository userRepository;
    @Autowired private TherapistRepository therapistRepository;
    @Autowired private LocationRepository locationRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userRegisterRequest)
    {
        User user = new User();
        user.setUid(userRegisterRequest.uid);
        user.setFullName(userRegisterRequest.name);
        user.setPhone(userRegisterRequest.phone);
        //user.setAddress(userRegisterRequest.address);
        user.setRole(userRegisterRequest.role);
        user.setEmail(userRegisterRequest.email);

        Location location = null;
        if (userRegisterRequest.locationId != null) {
            location = locationRepository.findById(userRegisterRequest.locationId).orElse(null);
            if (location != null) {

                user.setAddress(location.getName() + ", " + location.getCountry());
            }
        }
        user = userRepository.save(user);


        if("therapist".equalsIgnoreCase(userRegisterRequest.role))
        {
            Therapist therapist = new Therapist();
            therapist.setUser(user);
            therapist.setSpecializations(new ArrayList<>());
            therapist.setProfilePicture("https://randomuser.me/api/portraits/men/1.jpg");
            therapist.setBio("");
            therapist.setSessionCost(0.0);
            therapist.setRating(5.0);
            therapist.setVerified(false);
            therapist.setLocation(location);

            therapistRepository.save(therapist);
            System.out.println("Therapist inserted with user_id = " + user.getId());

        }

        return  ResponseEntity.ok("Registration to the DB is successfull");

    }


    @GetMapping("/locations")
    public ResponseEntity<?> getAllLocations() {
        return ResponseEntity.ok(locationRepository.findAll());
    }
}
