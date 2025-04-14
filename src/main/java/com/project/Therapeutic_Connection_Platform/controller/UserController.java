package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.jpaRepos.UserRepository;

import com.project.Therapeutic_Connection_Platform.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

private final UserRepository usersRepository;

public UserController(UserRepository usersRepository)
{
    this.usersRepository = usersRepository;
}


 /*   @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        usersRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    } */

    @GetMapping("/users/{uid}")
    public ResponseEntity<User> getUserByUid(@PathVariable String uid) {
        User user = usersRepository.findByFirebaseUid(uid);

        if(user == null)
        {
            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(user);

    }




}
