package com.jardin.api.controllers;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jardin.api.buckets.BucketName;
import com.jardin.api.exceptions.controllerExceptions.BadRequestException;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.repositories.GarmentRepository;
import com.jardin.api.services.GarmentService;
import com.jardin.api.services.S3FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//TODO: Refactor -> envolver el cuerpo de los metodos con un try catch, y retornar un Https.status adecuado. AHORA TODOS RETORNARN 201 !
@RestController
@RequestMapping("management/jardin-api/v1/garment")
@CrossOrigin("*")
public class GarmentController {

    static final String reactURL = "http://localhost:3000";

    private final GarmentRepository garmentRepo;
    private final S3FileStore s3FileStore;
    private final GarmentService garmentService;

    @Autowired
    private GarmentController(GarmentRepository garmentRepo, S3FileStore s3FileStore, GarmentService garmentService) {
        this.garmentRepo = garmentRepo;
        this.s3FileStore = s3FileStore;
        this.garmentService = garmentService;
    }

    @GetMapping("")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<List<Garment>> getAll(HttpServletResponse res) {
        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            List<Garment> garmentList = garmentRepo.findAll();
            return new ResponseEntity<>(garmentList,HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ArrayList<Garment>(),HttpStatus.UNAUTHORIZED);
        }

    }

    @CrossOrigin(origins = reactURL)
    @GetMapping("/{id}")
    private ResponseEntity<Garment> getById(@PathVariable("id") Long id, HttpServletResponse res) {
        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            try {
                Garment garment = garmentRepo.getOne(id);
                System.out.println(garment); // Si imprimo el garment y el id no retorno ninguno salta EntityNotFoundException
                return new ResponseEntity<>(garment, HttpStatus.ACCEPTED);
            } catch (EntityNotFoundException exception) {
                System.out.println(exception.getMessage());
                return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping("pageable/{limit}/{offset}")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<List<Garment>> getWithPagination(@PathVariable("limit") Integer limit,
                                                            @PathVariable("offset") Integer offset,
                                                            HttpServletResponse res) {

        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            ArrayList<Garment> garments = (ArrayList<Garment>) garmentService.getWithPagination(limit, offset);
            return new ResponseEntity<>(garments,HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ArrayList<Garment>(),HttpStatus.UNAUTHORIZED);
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

        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);

        if(isTokenValid) {
            List<Garment> resultsOfQuery = garmentRepo.findAll();
            //Check GENDER
            if(!gender.equals("")) {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getGender().equals(gender))
                        .collect(Collectors.toList());
            }

            //Check SIZE
            if(!size.equals("")){
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getSize().equals(size))
                        .collect(Collectors.toList());
            }

            //Check TYPE
            if(!type.equals("")) {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getType().equals(type))
                        .collect(Collectors.toList());
            }

            //Check GENDER
            if(!madeIn.equals("")) {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getMadeIn().equals(madeIn))
                        .collect(Collectors.toList());
            }

            //Check MADE IN
            if(!mainMaterial.equals("")) {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getMainMaterial().equals(mainMaterial))
                        .collect(Collectors.toList());
            }

            //Check PRICE FROM
            if(!priceFrom.equals(0)) {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getPrice() >= priceFrom)
                        .collect(Collectors.toList());
            }

            //Check PRICE TO
            if(priceTo.equals(0)) {
                int higherPrice = 1000000000;
                if(resultsOfQuery.size() > 0) {
                    higherPrice = resultsOfQuery.get(0).getPrice();

                    for (Garment garment : resultsOfQuery) {

                        if (garment.getPrice() > higherPrice) {
                            higherPrice = garment.getPrice();
                        }
                    }
                }
                int finalHigherPrice = higherPrice;
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getPrice() <= finalHigherPrice)
                        .collect(Collectors.toList());

            } else {
                resultsOfQuery = resultsOfQuery.stream()
                        .filter(garment -> garment.getPrice() <= priceTo)
                        .collect(Collectors.toList());
            }
            return new ResponseEntity<>(resultsOfQuery,HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new ArrayList<Garment>(),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<Garment> registerNewGarment(@RequestBody Garment garment,
                                                       @RequestParam MultipartFile file,
                                                       HttpServletResponse res) {

        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);

        if(isTokenValid) {
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
        } else {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.UNAUTHORIZED);
        }


    }

    @PostMapping("/post")
    @CrossOrigin(origins = reactURL)
    private ResponseEntity<Boolean> createGarment (@RequestBody Garment garment, HttpServletResponse res) {
        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            boolean created = false;
            try {
                Garment save = garmentRepo.save(garment);
                created = true;

            } catch (Exception e) {
                System.out.println(e);
            }
            return new ResponseEntity<>(created,HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(false,HttpStatus.UNAUTHORIZED);
        }

    }
    //Metodo put, que pasa por POST, ya que PUT enviaba error desde exios, se cambio para poder hacer la peticion.
    @PostMapping("/{id}")
    @CrossOrigin(origins = "*")
    @JsonIgnore
    private ResponseEntity<Garment> updateGarment(@RequestBody Garment garment,
                                                  @PathVariable("id") Long id,
                                                  HttpServletResponse res) {

        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            try {
                Garment toUpdate = garmentRepo.getOne(id);
                System.out.println(toUpdate);

                if(!toUpdate.getType().equals(garment.getType()) && (garment.getType() != null && !garment.getType().equals(""))) {
                    toUpdate.setType(garment.getType());
                }
                if(!toUpdate.getComment().equals(garment.getComment()) && (garment.getComment() != null && !garment.getComment().equals(""))) {
                    toUpdate.setComment(garment.getComment());
                }
                if(!toUpdate.getGender().equals(garment.getGender()) && garment.getGender() != null && !garment.getGender().equals("")) {
                    toUpdate.setGender(garment.getGender());
                }
                if(!toUpdate.getMadeIn().equals(garment.getMadeIn()) && (garment.getMadeIn() != null && !garment.getMadeIn().equals(""))) {
                    toUpdate.setMadeIn(garment.getMadeIn());
                }
                if(!toUpdate.getMainColor().equals(garment.getMainColor()) && (garment.getMainColor() != null && garment.getMainColor().equals(""))) {
                    toUpdate.setMainColor(garment.getMainColor());
                }
                if(!toUpdate.getMainMaterial().equals(garment.getMainMaterial()) && (garment.getMainMaterial() != null && !garment.getMainMaterial().equals(""))) {
                    toUpdate.setMainMaterial(garment.getMainMaterial());
                }
                if(!toUpdate.getPrice().equals(garment.getPrice()) && (garment.getPrice() != null && !garment.getPrice().equals(-1))) {
                    toUpdate.setPrice(garment.getPrice());
                }
                if(!toUpdate.getSize().equals(garment.getSize()) && (garment.getSize() != null && !garment.getSize().equals(""))) {
                    toUpdate.setSize(garment.getSize());
                }

                garmentRepo.update(id,toUpdate.getType(),toUpdate.getSize(),toUpdate.getMainColor(),toUpdate.getGender(),toUpdate.getMainMaterial(),toUpdate.getMadeIn(),toUpdate.getComment(),toUpdate.getPrice());

                return new ResponseEntity<>(garmentRepo.getOne(id),HttpStatus.ACCEPTED);

            } catch (EntityNotFoundException exception) {
                System.out.println("Dentro de catch");
                System.out.println(exception.getMessage());
                return new ResponseEntity<>(new Garment(), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.UNAUTHORIZED);
        }

    }
    //Este metodo deberia ser un delete, sin embargo el metodo DELETE me enviaba un error cuando lo enviada desde axios.
    @PostMapping("/delete/{id}")
    @CrossOrigin(origins=reactURL)
    private ResponseEntity<Garment> deleteGarmentById(@PathVariable("id") Long id, HttpServletResponse res) {
        String isTokenValidString = res.getHeader("isTokenValid");
        boolean isTokenValid = Boolean.parseBoolean(isTokenValidString);
        if(isTokenValid) {
            try {
                Garment garmentToDelete = garmentRepo.getOne(id);
                System.out.println(garmentToDelete); // Imprimo el recurso para hacer saltar la excepcion de ID NO EXISTENTE
                garmentRepo.deleteById(id);
                return new ResponseEntity<>(garmentToDelete,HttpStatus.ACCEPTED);
            } catch (EntityNotFoundException exception) {
                return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.UNAUTHORIZED);
        }
    }
}
