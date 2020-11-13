package com.jardin.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories({"com.jardin.api.repositories"})
@EntityScan({"com.jardin.api.model.entities", "com.jardin.api.services"})
public class ApiApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {




    }
}
