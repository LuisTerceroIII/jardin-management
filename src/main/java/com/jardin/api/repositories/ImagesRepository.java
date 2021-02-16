package com.jardin.api.repositories;

import com.jardin.api.model.entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {}
