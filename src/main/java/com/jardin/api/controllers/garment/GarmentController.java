package com.jardin.api.controllers.garment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jardin.api.exceptions.token.InvalidTokenException;
import com.jardin.api.models.entities.Garment;
import com.jardin.api.controllers.garment.especialResponses.CreateGarmentResponse;
import com.jardin.api.services.GarmentService;
import com.jardin.api.utilsFunctions.JwtTokenUtils;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("management/jardin-api/v1")
@CrossOrigin(origins = "*")
public class GarmentController{

  private final GarmentService garmentService;



  @Autowired
  private GarmentController(GarmentService garmentService) {
    this.garmentService = garmentService;
  }

  @GetMapping("/garment")
  @CrossOrigin(origins = "*")
  private ResponseEntity<List<Garment>> getAll(HttpServletResponse res) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getAll();
    } else {
     throw new InvalidTokenException();
    }
  }

  @CrossOrigin(origins = "*")
  @GetMapping("/garment/{id}")
  private ResponseEntity<Garment> getById(
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getByID(id);
    } else {
      throw new InvalidTokenException();
    }
  }

  @GetMapping("/garment/{limit}/{offset}")
  @CrossOrigin(origins = "*")
  private ResponseEntity<List<Garment>> getWithPagination(
    @PathVariable("limit") Integer limit,
    @PathVariable("offset") Integer offset,
    HttpServletResponse res
  ) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.getWithPagination(limit, offset);
    } else {
      throw new InvalidTokenException();
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
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
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
      throw new InvalidTokenException();
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
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
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
      throw new InvalidTokenException();
    }
  }

  @PostMapping("/garment")
  @CrossOrigin(origins = "*")
  private ResponseEntity<CreateGarmentResponse> createGarment(
    @RequestBody Garment garment,
    HttpServletResponse res
  ) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.createGarment(garment);
    } else {
      throw new InvalidTokenException();
    }
  }

  //Metodo put, que pasa por POST, ya que PUT enviaba error desde axios, se cambio para poder hacer la peticion.
  @PostMapping("/garment/{id}")
  @CrossOrigin(origins = "*")
  @JsonIgnore
  private ResponseEntity<Garment> updateGarment(
    @RequestBody Garment garment,
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.updateGarment(garment, id);
    } else {
      throw new InvalidTokenException();
    }
  }

  //Este metodo deberia ser un delete, sin embargo el metodo DELETE me enviaba un error cuando lo enviada desde axios.
  @DeleteMapping("/garment/{id}")
  @CrossOrigin(origins = "*")
  private ResponseEntity<Garment> deleteGarmentById(
    @PathVariable("id") Long id,
    HttpServletResponse res
  ) {
    boolean isTokenValid = JwtTokenUtils.isTokenValid(res);
    if (isTokenValid) {
      return garmentService.deleteGarment(id);
    } else {
      throw new InvalidTokenException();
    }
  }
}
