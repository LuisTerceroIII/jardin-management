package com.jardin.api.controllers;

import com.jardin.api.exceptions.controllerExceptions.InvalidTokenException;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.services.GarmentImagesService;
import com.jardin.api.services.TokenVerify;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("management/jardin-api/v1/garment-images")
public class GarmentImagesController {

    private final GarmentImagesService garmentImagesService;

    @Autowired
    public GarmentImagesController(GarmentImagesService garmentImagesService) {
        this.garmentImagesService = garmentImagesService;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Images> getAllLinks(@PathVariable("id") Long id, HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return garmentImagesService.downloadAllLink(id);
        } else {
            throw new InvalidTokenException();
        }
    }

    @GetMapping("/{id}/{nroImage}")
    private ResponseEntity<String> downloadPerLink(
            @PathVariable("id") Long id,
            @PathVariable("nroImage") int nroImage,
            HttpServletResponse res
    ) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return garmentImagesService.downloadLink(id, nroImage);
        } else {
            throw new InvalidTokenException();
        }
    }

    @PostMapping(
            path = "/{id}/{nroImage}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin(origins = "*")
    private ResponseEntity<Garment> uploadImages(
            @PathVariable("id") Long id,
            @PathVariable("nroImage") int nroImage,
            @RequestParam MultipartFile file,
            HttpServletResponse res
    ) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return garmentImagesService.uploadGarmentImage(id, nroImage, file);
        } else {
            throw new InvalidTokenException();
        }
    }

    @DeleteMapping("/{id}/{nroImage}")
    private ResponseEntity<Garment> deleteImage(
            @PathVariable("id") Long id,
            @PathVariable("nroImage") int nroImage,
            HttpServletResponse res
    ) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return garmentImagesService.deleteGarmentImage(id, nroImage);
        } else {
            throw new InvalidTokenException();
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Garment> deleteAllImages(
            @PathVariable("id") Long id,
            HttpServletResponse res
    ) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if (isTokenValid) {
            return garmentImagesService.deleteGarmentImages(id);
        } else {
            throw new InvalidTokenException();
        }
    }
}
