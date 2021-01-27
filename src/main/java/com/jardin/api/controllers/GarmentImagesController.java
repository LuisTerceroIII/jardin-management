package com.jardin.api.controllers;


import com.jardin.api.model.entities.Garment;
import com.jardin.api.services.GarmentImagesService;
import com.jardin.api.services.S3FileStore;
import com.jardin.api.services.TokenVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("management/jardin-api/v1/garment-images")
public class GarmentImagesController {

    private final GarmentImagesService garmentImagesService;

    @Autowired
    public GarmentImagesController(GarmentImagesService garmentImagesService) {
        this.garmentImagesService = garmentImagesService;
    }

    @PostMapping(path = "/{id}/{nroImage}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Garment> uploadImages(@PathVariable("id") Long id, @PathVariable("nroImage") int nroImage,
                                                 @RequestParam MultipartFile file, HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentImagesService.uploadGarmentImage(id,nroImage, file);
        } else {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}/{nroImage}")
    private ResponseEntity<String> downloadPerLink(@PathVariable("id") Long id, @PathVariable("nroImage") int nroImage,HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentImagesService.downloadLink(id,nroImage);
        } else {
            return new ResponseEntity<>("Invalid session",HttpStatus.UNAUTHORIZED);
        }
    }


}
