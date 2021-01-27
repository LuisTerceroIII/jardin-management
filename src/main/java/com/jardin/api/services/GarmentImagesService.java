package com.jardin.api.services;

import com.jardin.api.buckets.BucketName;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.repositories.GarmentRepository;
import com.jardin.api.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Service
public class GarmentImagesService {


    private final GarmentRepository garmentRepo;
    private final ImagesRepository imagesRepository;
    private final S3FileStore s3FileStore;

    private final String  bucketName = BucketName.IMAGE_BUCKET.getBucketName();

    @Autowired
    public GarmentImagesService(GarmentRepository garmentRepo,
                                   ImagesRepository imagesRepository,
                                   S3FileStore s3FileStore) {
        this.garmentRepo = garmentRepo;
        this.imagesRepository = imagesRepository;
        this.s3FileStore = s3FileStore;
    }

    public ResponseEntity<Garment> uploadGarmentImage(Long id, int nroImage, MultipartFile file) {

        Garment garment = garmentRepo.getOne(id);
        if(garment.getId() == null) {
            return new ResponseEntity<>(new Garment("","","","","","",0,""), HttpStatus.NOT_FOUND);
        }

        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        //path = bucketName/garment_id
        //filename = name of the file + random string
        String path = String.format("%s/%s", bucketName, id); //Vinculo imagen con pieza de ropa
        String filename = String.format("%s-%s", file.getName(), UUID.randomUUID());

        try {
            s3FileStore.save(path,filename, Optional.of(metadata), file.getInputStream());
            Images images = garment.getImages();
            if(images == null) {
                images = new Images(garment);
            }
            switch (nroImage) {
                case 1:
                    images.setLinkImage1(filename);
                    break;
                case 2:
                    images.setLinkImage2(filename);
                    break;
                case 3:
                    images.setLinkImage3(filename);
                    break;
                case 4:
                    images.setLinkImage4(filename);
                    break;
                case 5:
                    images.setLinkImage5(filename);
                    break;
                case 6:
                    images.setLinkImage6(filename);
                    break;
                default:
                    return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.BAD_REQUEST);
            }
            imagesRepository.save(images);
            garment.setImages(images);
            garmentRepo.save(garment);
            System.out.println(garment);

        } catch (IOException e) {
            return new ResponseEntity<>(new Garment("","","","","","",0,""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(garment,HttpStatus.CREATED);
    }

    public ResponseEntity<String> downloadLink(Long id, int imageNumber) {
        try {
            String region = "s3-sa-east-1";
            String amazon = "amazonaws.com";
            String   fileName = "";

            Garment garment = garmentRepo.getOne(id);
            System.out.println(garment); // Imprimo el recurso para hacer saltar la excepcion de ID NO EXISTENTE

            Images images = garment.getImages();

            if(images == null) {
                return new ResponseEntity<>("No images found",HttpStatus.BAD_REQUEST);
            }

            switch (imageNumber) {
                case 1:
                    fileName = images.getLinkImage1();
                    break;
                case 2:
                    fileName = images.getLinkImage2();
                    break;
                case 3:
                    fileName = images.getLinkImage3();
                    break;
                case 4:
                    fileName = images.getLinkImage4();
                    break;
                case 5:
                    fileName = images.getLinkImage5();
                    break;
                case 6:
                    fileName = images.getLinkImage6();
                    break;
                default:
                    fileName = "";
            }

            if(fileName.equals("")) {
                return new ResponseEntity<>("Number of image does not found, range is 1 - 6",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("https://"+bucketName+"."+ region +"."+ amazon +"/"+id.toString() + "/"+fileName ,HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("ID not found",HttpStatus.NOT_FOUND);
        }
    }
}
