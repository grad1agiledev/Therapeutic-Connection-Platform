package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.UserUpdateRequest;
import com.project.Therapeutic_Connection_Platform.jpaRepos.LocationRepository;
import com.project.Therapeutic_Connection_Platform.jpaRepos.UserRepository;

import com.project.Therapeutic_Connection_Platform.model.Location;
import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class UserController {

    private final UserRepository usersRepository;


    public UserController(UserRepository usersRepository) {
        this.usersRepository = usersRepository;

    }


 /*   @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        usersRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    } */

    @GetMapping("/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        User user = usersRepository.findByFirebaseUid(uid);

        if (user == null) {
            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(user);

    }


    /*
    Updating the patients if accessed a request
     */
    @PutMapping("/{uid}")
    public ResponseEntity<?> updateUser(
            @PathVariable String uid,
            @RequestBody UserUpdateRequest req) {

        User user = usersRepository.findByFirebaseUid(uid);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        user.setFullName(req.fullName);
        user.setPhone(req.phone);

        user.setAddress(req.address);

        usersRepository.save(user);
        return ResponseEntity.ok().build();


    }

}
