package com.jardin.api.controllers;

import com.jardin.api.model.entities.User;
import com.jardin.api.model.entities.pojos.post.UserToLogout;
import com.jardin.api.model.responses.LoginResponse;
import com.jardin.api.repositories.UserRepository;
import com.jardin.api.services.AuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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
    @PostMapping("/loginToken")
    @CrossOrigin(origins = "http://localhost:3000")
    private ResponseEntity<LoginResponse> loginWithToken(@RequestBody User user) {

        LoginResponse errorResponse = new LoginResponse(false,"0");

        User registerUser = userRepository.getUserByUsername(user.getUsername());

        if (registerUser == null) {
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        boolean correctPassword = BCrypt.checkpw(user.getPassword(), registerUser.getPassword());

        if (correctPassword) {
            String secretKey = "asegurandoJardinAPI2021LuisHECTOResPINOZAnaVaRRete9488888";
            String sessionToken = Jwts.builder()
                    .setSubject("Jardin")
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();

            LoginResponse loginResponse = new LoginResponse(true,sessionToken);
            return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    private ResponseEntity<LoginResponse> login(@RequestBody User user) {

        String sessionToken = UUID.randomUUID().toString();
        LoginResponse errorResponse = new LoginResponse(false,"0");
        User registerUser = userRepository.getUserByUsername(user.getUsername());

        if (registerUser == null) {
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        boolean correctPassword = BCrypt.checkpw(user.getPassword(), registerUser.getPassword());

        if (correctPassword && registerUser.getOnline()) { // desconecto por reconeccion de usuario logeado.
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); //409
        } else if (correctPassword && !registerUser.getOnline()) {
           /* userRepository.updateOnlineStatus(true, registerUser.getId());
            userRepository.updateSessionToken(sessionToken,registerUser.getId());*/
            registerUser.setSessionToken(sessionToken);
            registerUser.setOnline(true);
            userRepository.save(registerUser);
            User newStateUser = userRepository.getOne(registerUser.getId());
            System.out.println("Nuevo estado : "+ newStateUser);
            LoginResponse loginResponse = new LoginResponse(true,sessionToken);
            return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @PostMapping("/logout")
    @CrossOrigin(origins = "http://localhost:3000")
    private ResponseEntity<Boolean> logout(@RequestBody User user) {
        User registerUser = userRepository.getUserByUsername(user.getUsername());
        if (registerUser == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        boolean correctPassword = BCrypt.checkpw(user.getPassword(), registerUser.getPassword());

        if (correctPassword && registerUser.getOnline()) { // desconecto por reconeccion de usuario logeado.
            userRepository.updateOnlineStatus(false, registerUser.getId());
            return new ResponseEntity<>(true, HttpStatus.OK); //200
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/logoutWithToken")
    @CrossOrigin(origins = "http://localhost:3000")
    private ResponseEntity<Boolean> logoutWithToken(@RequestBody UserToLogout user) {
        System.out.println(user);
        User registerUser = userRepository.getUserByUsername(user.getUsername());
        System.out.println(registerUser);
        if (registerUser == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        String toCheckSessionToken = user.getSessionToken();
        System.out.println("post token : " + toCheckSessionToken);
        String sessionToken = registerUser.getSessionToken();
        System.out.println("Database token : " + sessionToken);

        boolean tokenIsCorrect = toCheckSessionToken.equals(sessionToken);
        System.out.println(tokenIsCorrect);
        if (tokenIsCorrect && registerUser.getOnline()) { // desconecto por reconeccion de usuario logeado.
            userRepository.updateOnlineStatus(false, registerUser.getId());
            userRepository.updateSessionToken("0",registerUser.getId());
            System.out.println("DENTRO DE CAMBIOS!!!");
            return new ResponseEntity<>(true, HttpStatus.OK); //200
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
