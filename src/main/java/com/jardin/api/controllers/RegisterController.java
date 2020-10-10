package com.jardin.api.controllers;

import com.jardin.api.model.entities.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("register")
public class RegisterController {

    @PostMapping("")
    private Boolean register(@RequestBody Account newUser) {
        return false;
    }

    @GetMapping("/get")
    private String hola() {
        return "Hola";
    }

}
