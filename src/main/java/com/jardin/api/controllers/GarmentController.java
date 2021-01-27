package com.jardin.api.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.services.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jardin.api.services.TokenVerify;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@RequestMapping("management/jardin-api/v1/garment")
@CrossOrigin("*")
public class GarmentController {

    private final GarmentService garmentService;
    private static final String reactURL = "http://localhost:3000";

    //Respuesta enviada cuando token es invalido
    ResponseEntity<Garment> tokenInvalidGarmentResponse = new ResponseEntity<>(new Garment("", "", "", "", "", "", 0, ""), HttpStatus.UNAUTHORIZED);
    ResponseEntity<List<Garment>> tokenInvalidArrayResponse = new ResponseEntity<>(new ArrayList<Garment>(), HttpStatus.UNAUTHORIZED);

    @Autowired
    private GarmentController(GarmentService garmentService) {
        this.garmentService = garmentService;
    }

    @GetMapping("")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<List<Garment>> getAll(HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.getAll();
        } else {
            return tokenInvalidArrayResponse;
        }
    }

    @CrossOrigin(origins = reactURL)
    @GetMapping("/{id}")
    private ResponseEntity<Garment> getById(@PathVariable("id") Long id, HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.getByID(id);
        } else {
            return tokenInvalidGarmentResponse;
        }
    }

    @GetMapping("pageable/{limit}/{offset}")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<List<Garment>> getWithPagination(@PathVariable("limit") Integer limit, @PathVariable("offset") Integer offset,
                                                            HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.getWithPagination(limit,offset);
        } else {
            return tokenInvalidArrayResponse;
        }
    }

    @CrossOrigin(origins = reactURL)
    @GetMapping("/search")
    private ResponseEntity<List<Garment>> searchResolver(@RequestParam(required = false) String gender,
                                                         @RequestParam(required = false) String size,
                                                         @RequestParam(required = false) String type,
                                                         @RequestParam(required = false) String madeIn,
                                                         @RequestParam(required = false) String mainMaterial,
                                                         @RequestParam(required = false) Integer priceFrom,
                                                         @RequestParam(required = false) Integer priceTo,
                                                         HttpServletResponse res) {

        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.searchGarment(gender, size, type, madeIn, mainMaterial, priceFrom, priceTo);
        } else {
            return tokenInvalidArrayResponse;
        }
    }

    //TODO: Rotarnar ResponseEntity<CreateGarmentResponse>
    // CreateGarmentResponse -> Garment garment, boolean created (retornar un objeto vacio si no se puede crear).
    @PostMapping("/post")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<Boolean> createGarment(@RequestBody Garment garment, HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.createGarment(garment);
        } else {
            return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
        }
    }

    //Metodo put, que pasa por POST, ya que PUT enviaba error desde exios, se cambio para poder hacer la peticion.
    @PostMapping("/{id}")
    @CrossOrigin(origins = "*")
    @JsonIgnore
    private ResponseEntity<Garment> updateGarment(@RequestBody Garment garment, @PathVariable("id") Long id,
                                                  HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.updateGarment(garment, id);
        } else {
            return tokenInvalidGarmentResponse;
        }
    }

    //Este metodo deberia ser un delete, sin embargo el metodo DELETE me enviaba un error cuando lo enviada desde axios.
    @PostMapping("/delete/{id}")
    @CrossOrigin(origins=reactURL)
    private ResponseEntity<Garment> deleteGarmentById(@PathVariable("id") Long id, HttpServletResponse res) {
        boolean isTokenValid = TokenVerify.isTokenValid(res);
        if(isTokenValid) {
            return garmentService.deleteGarment(id);
        } else {
            return tokenInvalidGarmentResponse;
        }
    }



}
