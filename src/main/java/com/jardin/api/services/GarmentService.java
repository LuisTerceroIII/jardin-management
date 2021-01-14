package com.jardin.api.services;

import com.jardin.api.model.entities.Garment;
import com.jardin.api.repositories.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class GarmentService {

    GarmentRepository garmentRepo;
    EntityManager entityManager;

    @Autowired
     public GarmentService(GarmentRepository garmentRepository,EntityManager entityManager) {
        this.garmentRepo = garmentRepository;
        this.entityManager = entityManager;
    }

    public List<Garment> getWithPagination(Integer limit, Integer offset) {
        return garmentRepo.getGarmentWithPagination(limit, offset);

    }
}
