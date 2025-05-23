package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.UserEmailResponse;
import com.project.Therapeutic_Connection_Platform.dto.UserRegisterRequest;
import com.project.Therapeutic_Connection_Platform.dto.UserUpdateRequest;
import com.project.Therapeutic_Connection_Platform.jpaRepos.LocationRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.UserRepository;

import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://therapeutic-connection-platform-1.onrender.com", allowCredentials = "true")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable("role") String role) {
        try {
            List<User> users = userRepository.findAll().stream()
                    .filter(user -> role.equalsIgnoreCase(user.getRole()))
                    .toList();
            
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/uid/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        User user = userRepository.findByFirebaseUid(uid);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/uid/{uid}")
    public ResponseEntity<?> updateUser(
            @PathVariable String uid,
            @RequestBody UserUpdateRequest req) {

        User user = userRepository.findByFirebaseUid(uid);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        user.setFullName(req.fullName);
        user.setPhone(req.phone);
        user.setAddress(req.address);

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest req) {
        if (userRepository.findByFirebaseUid(req.uid) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        User user = new User();
        user.setUid(req.uid);
        user.setFullName(req.name);
        user.setEmail(req.email);
        user.setPhone(req.phone);
        user.setRole(req.role);
        user.setAddress(req.address);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/patients")
    public List<User> getAllPatients() {
        return userRepository.findByRole("patient");
    }


    @GetMapping("/users/idByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with email " + email + " not found");
            }

            return ResponseEntity.ok().body(new UserEmailResponse(user.getUid(), user.getFullName(), user.getRole()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }


}
