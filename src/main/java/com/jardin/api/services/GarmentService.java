package com.jardin.api.services;

import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.responses.CreateGarmentResponse;
import com.jardin.api.repositories.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarmentService {

    GarmentRepository garmentRepo;

    @Autowired
     public GarmentService(GarmentRepository garmentRepository) {
        this.garmentRepo = garmentRepository;
    }

    public ResponseEntity<List<Garment>> getAll() {
        List<Garment> garmentList = garmentRepo.findAll();
        return new ResponseEntity<>(garmentList,HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Garment> getByID(Long id) {
        try {
            Garment garment = garmentRepo.getOne(id);
            System.out.println(garment); // Si imprimo el garment y el id no retorno ninguno salta EntityNotFoundException
            return new ResponseEntity<>(garment, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<List<Garment>> searchGarment(String gender, String size, String type, String madeIn, String mainMaterial, Integer priceFrom, Integer priceTo) {
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
    }


    public ResponseEntity<CreateGarmentResponse> createGarment(Garment garment) {
        CreateGarmentResponse response = new CreateGarmentResponse();
        try {
            Garment save = garmentRepo.save(garment);
            response.setCreated(true);
            response.setCreatedGarment(save);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            response.setCreated(false);
            response.setCreatedGarment(new Garment("", "", "", "", "", "", 0, ""));
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    public ResponseEntity<Garment> deleteGarment(Long id) {
        try {
            Garment garmentToDelete = garmentRepo.getOne(id);
            System.out.println(garmentToDelete); // Imprimo el recurso para hacer saltar la excepcion de ID NO EXISTENTE
            garmentRepo.deleteById(id);
            return new ResponseEntity<>(garmentToDelete, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Garment> updateGarment(Garment garment, Long id) {
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
    }

    public ResponseEntity<List<Garment>> getWithPagination(Integer limit, Integer offset) {
        ArrayList<Garment> garments = (ArrayList<Garment>) garmentRepo.getGarmentWithPagination(limit, offset);
        return new ResponseEntity<>(garments,HttpStatus.ACCEPTED);

    }
}
