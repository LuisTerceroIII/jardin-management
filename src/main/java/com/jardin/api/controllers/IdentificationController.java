package com.jardin.api.controllers;

import com.jardin.api.model.entities.User;
import com.jardin.api.repositories.UserRepository;
import com.jardin.api.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("management/jardin-api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class IdentificationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Autowired
    private IdentificationController(AuthenticationService authenticationService, UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    private ResponseEntity<User> register(@RequestBody User user) {
        String encryptPass = authenticationService.cryptPassword(user.getPassword());
        user.setEmail(user.getEmail().toLowerCase());
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encryptPass);
        User newUser = new User();
        try {
            newUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    private ResponseEntity<Boolean> login(@RequestBody User user) {
        System.out.println(user.getPassword());
        User registerUser = userRepository.getUserByUsername(user.getUsername());
        return new ResponseEntity<>(BCrypt.checkpw(user.getPassword(),registerUser.getPassword()),HttpStatus.ACCEPTED);
    }
}
