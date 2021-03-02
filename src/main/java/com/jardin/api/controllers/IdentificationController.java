package com.jardin.api.controllers;

import com.jardin.api.exceptions.controllerExceptions.InvalidTokenException;
import com.jardin.api.model.entities.User;

import com.jardin.api.model.responses.LoginResponse;
import com.jardin.api.repositories.UserRepository;
import com.jardin.api.services.AuthenticationService;
import com.jardin.api.services.TokenVerify;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDate;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("management/jardin-api/v1")
@CrossOrigin(origins = "*")
public class IdentificationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Autowired
    private IdentificationController(
            AuthenticationService authenticationService,
            UserRepository userRepository
    ) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    private ResponseEntity<User> register(@RequestBody User user) {
        String encryptPass = authenticationService.cryptPassword(
                user.getPassword()
        );
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
    private ResponseEntity<LoginResponse> loginWithToken(@RequestBody User user) {
        LoginResponse errorResponse = new LoginResponse(false, "0");

        User registerUser = userRepository.getUserByUsername(user.getUsername());

        if (registerUser == null) {
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        boolean correctPassword = BCrypt.checkpw(
                user.getPassword(),
                registerUser.getPassword()
        );

        if (correctPassword) {
            String secretKey =
                    "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";
            String sessionToken = Jwts
                    .builder()
                    .setSubject("Jardin")
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();

            LoginResponse loginResponse = new LoginResponse(true, sessionToken);
            return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/validateSession")
    public ResponseEntity<Boolean> validateSession(HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        } else {
            throw new InvalidTokenException();
        }
    }
}
