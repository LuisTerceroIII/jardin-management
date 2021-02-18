package com.jardin.api.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.responses.CreateGarmentResponse;
import com.jardin.api.services.GarmentService;
import com.jardin.api.services.TokenVerify;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("management/jardin-api/v1")
@CrossOrigin(origins = "*")
public class GarmentController {

  private final GarmentService garmentService;
  private static final String reactURL = "http://localhost:3000";

  //Respuesta enviada cuando token es invalido
  ResponseEntity<Garment> invalidTokenOneGarmentResponse = new ResponseEntity<>(
    new Garment("", "", "", "", "", "", 0, ""),
    HttpStatus.UNAUTHORIZED
  );
  ResponseEntity<List<Garment>> invalidTokenArrayResponse = new ResponseEntity<>(
    new ArrayList<Garment>(),
    HttpStatus.UNAUTHORIZED
  );
  ResponseEntity<CreateGarmentResponse> invalidTokenCreateRequest = new ResponseEntity<>(
    new CreateGarmentResponse(
      false,
      new Garment("", "", "", "", "", "", 0, "")
    ),
    HttpStatus.UNAUTHORIZED
  );

  @Autowired
  private GarmentController(GarmentService garmentService) {
    this.garmentService = garmentService;
  }

  @GetMapping("/garment")
  @CrossOrigin(origins = "*")
  private ResponseEntity<List<Garment>> getAll(HttpServletResponse res) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getAll();
    } else {
      return invalidTokenArrayResponse;
    }
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/garment/{id}")
  private ResponseEntity<Garment> getById(
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getByID(id);
    } else {
      return invalidTokenOneGarmentResponse;
    }
  }

  @GetMapping("/garment/{limit}/{offset}")
  @CrossOrigin(origins = "*")
  private ResponseEntity<List<Garment>> getWithPagination(
    @PathVariable("limit") Integer limit,
    @PathVariable("offset") Integer offset,
    HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getWithPagination(limit, offset);
    } else {
      return invalidTokenArrayResponse;
    }
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/garments/{limit}/{offset}")
  private ResponseEntity<List<Garment>> searchResolver2(
          @RequestParam(required = false) String gender,
          @RequestParam(required = false) String size,
          @RequestParam(required = false) String type,
          @RequestParam(required = false) String madeIn,
          @RequestParam(required = false) String mainMaterial,
          @RequestParam(required = false) Integer priceFrom,
          @RequestParam(required = false) Integer priceTo,
          @PathVariable("limit") Integer limit,
          @PathVariable("offset") Integer offset,
          HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.searchGarment(
              gender,
              size,
              type,
              madeIn,
              mainMaterial,
              priceFrom,
              priceTo,
              limit,
              offset
      );
    } else {
      return invalidTokenArrayResponse;
    }
  }
  @CrossOrigin(origins = "*")
  @GetMapping("/garments/search/count-rows")
  private ResponseEntity<Long> countRowsSearchGarment(
          @RequestParam(required = false) String gender,
          @RequestParam(required = false) String size,
          @RequestParam(required = false) String type,
          @RequestParam(required = false) String madeIn,
          @RequestParam(required = false) String mainMaterial,
          @RequestParam(required = false) Integer priceFrom,
          @RequestParam(required = false) Integer priceTo,
          HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.countRowsSearchGarment(
              gender,
              size,
              type,
              madeIn,
              mainMaterial,
              priceFrom,
              priceTo
      );
    } else {
      return new ResponseEntity<>(0L,HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping("/garment")
  @CrossOrigin(origins = "*")
  private ResponseEntity<CreateGarmentResponse> createGarment(
    @RequestBody Garment garment,
    HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.createGarment(garment);
    } else {
      return invalidTokenCreateRequest;
    }
  }

  //Metodo put, que pasa por POST, ya que PUT enviaba error desde exios, se cambio para poder hacer la peticion.
  @PostMapping("/garment/{id}")
  @CrossOrigin(origins = "*")
  @JsonIgnore
  private ResponseEntity<Garment> updateGarment(
    @RequestBody Garment garment,
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.updateGarment(garment, id);
    } else {
      return invalidTokenOneGarmentResponse;
    }
  }

  //Este metodo deberia ser un delete, sin embargo el metodo DELETE me enviaba un error cuando lo enviada desde axios.
  @DeleteMapping("/garment/{id}")
  @CrossOrigin(origins = "*")
  private ResponseEntity<Garment> deleteGarmentById(
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = TokenVerify.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.deleteGarment(id);
    } else {
      return invalidTokenOneGarmentResponse;
    }
  }
}
