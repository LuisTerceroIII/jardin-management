package com.jardin.api.controllers;

import com.jardin.api.buckets.BucketName;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.repositories.GarmentRepository;
import com.jardin.api.services.S3FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("management/jardin-api/v1/garment")
public class GarmentController {

    private final GarmentRepository garmentRepo;
    private final S3FileStore s3FileStore;

    @Autowired
    private GarmentController(GarmentRepository garmentRepo, S3FileStore s3FileStore) {
        this.garmentRepo = garmentRepo;
        this.s3FileStore = s3FileStore;
    }

    @GetMapping("")
    private ResponseEntity<List<Garment>> getAll() {
        List<Garment> garmentList = garmentRepo.findAll();
        return new ResponseEntity<>(garmentList,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{id}")
    private ResponseEntity<Garment> getById(@PathVariable("id") Long id) {
        Garment garment = garmentRepo.getOne(id);
        return new ResponseEntity<>(garment, HttpStatus.ACCEPTED);
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<Garment> registerNewGarment(@RequestBody Garment garment, @RequestParam MultipartFile file) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        Garment newGarment = garmentRepo.save(garment);

        String path = String.format("%s/%s", BucketName.IMAGE_BUCKET.getBucketName(), newGarment.getId()); //Vinculo imagen con pieza de ropa
        //filename, file name + random string
        String filename = String.format("%s-%s", file.getName(), UUID.randomUUID());

        try {
            s3FileStore.save(path,filename, Optional.of(metadata),file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return new ResponseEntity<>(garment,HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<Garment> updateGarment(@RequestBody Garment garment, @PathVariable("id") Long id) {
        Garment toUpdate = garmentRepo.getOne(id);
        if(!toUpdate.getType().equals(garment.getType()) && garment.getType() != null ) {
            toUpdate.setType(garment.getType());
        }
        if(!toUpdate.getComment().equals(garment.getComment()) && garment.getComment() != null) {
            toUpdate.setComment(garment.getComment());
        }
        if(!toUpdate.getGender().equals(garment.getGender()) && garment.getGender() != null) {
            toUpdate.setGender(garment.getGender());
        }
        if(!toUpdate.getMadeIn().equals(garment.getMadeIn()) && garment.getMadeIn() != null) {
            toUpdate.setMadeIn(garment.getMadeIn());
        }
        if(!toUpdate.getMainColor().equals(garment.getMainColor()) && garment.getMainColor() != null) {
            toUpdate.setMainColor(garment.getMainColor());
        }
       if(!toUpdate.getMainMaterial().equals(garment.getMainMaterial()) && garment.getMainMaterial() != null) {
           toUpdate.setMainMaterial(garment.getMainMaterial());
       }
       if(!toUpdate.getPrice().equals(garment.getPrice()) && garment.getPrice() != null) {
           toUpdate.setPrice(garment.getPrice());
       }
       if(!toUpdate.getSize().equals(garment.getSize()) && garment.getSize() != null) {
           toUpdate.setSize(garment.getSize());
       }

       garmentRepo.update(id,toUpdate.getType(),toUpdate.getSize(),toUpdate.getMainColor(),toUpdate.getGender(),toUpdate.getMainMaterial(),toUpdate.getMadeIn(),toUpdate.getComment(),toUpdate.getPrice());

        return new ResponseEntity<>(garmentRepo.getOne(id),HttpStatus.ACCEPTED);

    }

}
