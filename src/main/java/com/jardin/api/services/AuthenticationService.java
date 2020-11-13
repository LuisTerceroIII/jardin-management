package com.jardin.api.services;

import com.jardin.api.model.entities.User;
import com.jardin.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository userRepository;

    @Autowired
    private AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean checkPassword(Long userId,String userPassword) {
        String cryptPass  =  userRepository.findById(userId).get().getPassword();
     return BCrypt.checkpw(userPassword,cryptPass);
    }

    public String cryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword,BCrypt.gensalt());
    }
}
