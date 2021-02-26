package com.jardin.api;

import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.repositories.GarmentRepository;
import com.jardin.api.repositories.ImagesRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories({ "com.jardin.api.repositories" })
@EntityScan({ "com.jardin.api.model.entities", "com.jardin.api.services" })
public class ApiApplication extends SpringBootServletInitializer   {

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ApiApplication.class);
  }


}
