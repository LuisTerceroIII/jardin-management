package com.jardin.api.services;

import com.jardin.api.exceptions.controllerExceptions.*;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.model.responses.CreateGarmentResponse;
import com.jardin.api.repositories.GarmentRepository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import com.jardin.api.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GarmentService {

    GarmentRepository garmentRepo;

    ImagesRepository imagesRepository;

    @Autowired
    public GarmentService(GarmentRepository garmentRepository, ImagesRepository imagesRepository) {
        this.garmentRepo = garmentRepository;
        this.imagesRepository = imagesRepository;
    }

    public ResponseEntity<List<Garment>> getAll() {
        List<Garment> garmentList = garmentRepo.findAll();
        if(garmentList.isEmpty()) {
            throw new EmptyResourceListException();
        } else {
            return new ResponseEntity<>(garmentList, HttpStatus.ACCEPTED);
        }
    }

    public ResponseEntity<Garment> getByID(Long id) {
        try {
            Garment garment = garmentRepo.getOne(id);
            System.out.println(garment); // Si imprimo el garment y el id no retorno ninguno salta EntityNotFoundException
            return new ResponseEntity<>(garment, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException exception) {
            throw new NoSuchElementFoundException(id.toString());
        }
    }

    public ResponseEntity<CreateGarmentResponse> createGarment(Garment garment) {
        CreateGarmentResponse response = new CreateGarmentResponse();
        try {
            Garment save = garmentRepo.save(garment);
            response.setCreated(true);
            response.setCreatedGarment(save);
        } catch (Exception e) {
            throw new ResourceCreationException(garment.getClass().getSimpleName());
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<Garment> deleteGarment(Long id) {
        try {
            Garment garmentToDelete = garmentRepo.getOne(id);
            Images images = garmentToDelete.getImages();
            System.out.println(garmentToDelete); // Imprimo el recurso para hacer saltar la excepcion de ID NO EXISTENTE
            garmentRepo.deleteById(id);
            if (images != null) {
                imagesRepository.delete(images);
            }
            return new ResponseEntity<>(garmentToDelete, HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException exception) {
            throw new NoSuchElementFoundException(id.toString());
        } catch (Exception e) {
            throw new ResourceDeleteException(Garment.class.getSimpleName(),id);
        }
    }

    public ResponseEntity<Garment> updateGarment(Garment garment, Long id) {
        try {
            Garment toUpdate = garmentRepo.getOne(id);
            System.out.println(toUpdate); // Si no hay reesultado salta EntityNotFoundException

            if (
                    !toUpdate.getType().equals(garment.getType()) &&
                            (garment.getType() != null && !garment.getType().equals(""))
            ) {
                toUpdate.setType(garment.getType());
            }
            if (
                    !toUpdate.getComment().equals(garment.getComment()) &&
                            (garment.getComment() != null && !garment.getComment().equals(""))
            ) {
                toUpdate.setComment(garment.getComment());
            }
            if (
                    !toUpdate.getGender().equals(garment.getGender()) &&
                            garment.getGender() != null &&
                            !garment.getGender().equals("")
            ) {
                toUpdate.setGender(garment.getGender());
            }
            if (
                    !toUpdate.getMadeIn().equals(garment.getMadeIn()) &&
                            (garment.getMadeIn() != null && !garment.getMadeIn().equals(""))
            ) {
                toUpdate.setMadeIn(garment.getMadeIn());
            }
            if (
                    !toUpdate.getMainColor().equals(garment.getMainColor()) &&
                            (garment.getMainColor() != null && garment.getMainColor().equals(""))
            ) {
                toUpdate.setMainColor(garment.getMainColor());
            }
            if (
                    !toUpdate.getMainMaterial().equals(garment.getMainMaterial()) &&
                            (
                                    garment.getMainMaterial() != null &&
                                            !garment.getMainMaterial().equals("")
                            )
            ) {
                toUpdate.setMainMaterial(garment.getMainMaterial());
            }
            if (
                    !toUpdate.getPrice().equals(garment.getPrice()) &&
                            (garment.getPrice() != null && !garment.getPrice().equals(-1))
            ) {
                toUpdate.setPrice(garment.getPrice());
            }
            if (
                    !toUpdate.getSize().equals(garment.getSize()) &&
                            (garment.getSize() != null && !garment.getSize().equals(""))
            ) {
                toUpdate.setSize(garment.getSize());
            }

            garmentRepo.update(
                    id,
                    toUpdate.getType(),
                    toUpdate.getSize(),
                    toUpdate.getMainColor(),
                    toUpdate.getGender(),
                    toUpdate.getMainMaterial(),
                    toUpdate.getMadeIn(),
                    toUpdate.getComment(),
                    toUpdate.getPrice()
            );

            return new ResponseEntity<>(garmentRepo.getOne(id), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException exception) {
            throw new NoSuchElementFoundException(id.toString());
        } catch (Exception e) {
            throw new ResourceUpdateException(Garment.class.getSimpleName(),id);
        }
    }

    public ResponseEntity<List<Garment>> getWithPagination(Integer limit,Integer offset) {
        ArrayList<Garment> garments = (ArrayList<Garment>) garmentRepo.getGarmentWithPagination(limit, offset);
        if(garments.isEmpty()) {
            throw new EmptyResourceListException();
        } else {
            return new ResponseEntity<>(garments, HttpStatus.ACCEPTED);
        }
    }

    public ResponseEntity<List<Garment>> searchGarment(
            String gender,
            String size,
            String type,
            String madeIn,
            String mainMaterial,
            Integer priceFrom,
            Integer priceTo,
            Integer limit,
            Integer offset
    ) {
        List<Garment> resultsOfQuery = garmentRepo.searchResolver(type, gender, size, madeIn, mainMaterial, priceFrom, priceTo, limit, offset);
        if(resultsOfQuery.isEmpty()) {
            throw new EmptyResourceListException();
        } else {
            return new ResponseEntity<>(resultsOfQuery, HttpStatus.ACCEPTED);
        }

    }

    //Cantidad de elementos que se generan en una query.
    //Trabaja con el front-end, ahi se necesita la cantidad de elementos que genera una busqueda para hacer la paginacion.
    public ResponseEntity<Long> countRowsSearchGarment(
            String gender,
            String size,
            String type,
            String madeIn,
            String mainMaterial,
            Integer priceFrom,
            Integer priceTo
    ) {
        Long cantRowsAffected = garmentRepo.searchResolverNoPagination(type, gender, size, madeIn, mainMaterial, priceFrom, priceTo);
        return new ResponseEntity<>(cantRowsAffected, HttpStatus.ACCEPTED);

    }

}
