package com.jardin.api;

import com.jardin.api.model.entities.Account;
import com.jardin.api.model.entities.UserRole;
import com.jardin.api.repositories.AccountsRepository;
import com.jardin.api.repositories.UserRolesRepository;
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
@EntityScan("com.jardin.api.model.entities")
public class ApiApplication implements CommandLineRunner {

    @Autowired
    private UserRolesRepository userRolesRepo;

    @Autowired
    private AccountsRepository accountsRepo;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        UserRole owner = userRolesRepo.getOne(1L);
        System.out.println(owner);

        Account owenerAccount = accountsRepo.getOne(1L);
        System.out.println(owenerAccount);



    }
}
