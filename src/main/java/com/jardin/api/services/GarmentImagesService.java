package com.jardin.api.services;

import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.jardin.api.buckets.BucketName;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.repositories.GarmentRepository;
import com.jardin.api.repositories.ImagesRepository;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GarmentImagesService {

  private final GarmentRepository garmentRepo;
  private final ImagesRepository imagesRepository;
  private final S3FileStore s3FileStore;

  private final String bucketName = BucketName.IMAGE_BUCKET.getBucketName();
  ResponseEntity<Garment> garmentNotFound = new ResponseEntity<>(
    new Garment("", "", "", "", "", "", 0, ""),
    HttpStatus.NOT_FOUND
  );
  ResponseEntity<Garment> imagesNotFound = new ResponseEntity<>(
    new Garment("", "", "", "", "", "", 0, ""),
    HttpStatus.BAD_REQUEST
  );

  @Autowired
  public GarmentImagesService(
    GarmentRepository garmentRepo,
    ImagesRepository imagesRepository,
    S3FileStore s3FileStore
  ) {
    this.garmentRepo = garmentRepo;
    this.imagesRepository = imagesRepository;
    this.s3FileStore = s3FileStore;
  }

  public ResponseEntity<Garment> uploadGarmentImage(
    Long id,
    int nroImage,
    MultipartFile file
  ) {
    Garment garment = garmentRepo.getOne(id);
    if (garment.getId() == null) {
      return new ResponseEntity<>(
        new Garment("", "", "", "", "", "", 0, ""),
        HttpStatus.NOT_FOUND
      );
    }

    Map<String, String> metadata = new HashMap<>();
    metadata.put("Content-Type", file.getContentType());
    metadata.put("Content-Length", String.valueOf(file.getSize()));

    //path = bucketName/garment_id
    //filename = name of the file + random string
    String path = String.format("%s/%s", bucketName, id); //Vinculo imagen con pieza de ropa
    String filename = String.format("%s-%s", file.getName(), UUID.randomUUID());

    try {
      s3FileStore.save(
        path,
        filename,
        Optional.of(metadata),
        file.getInputStream()
      );
      Images images = garment.getImages();
      if (images == null) {
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
          return new ResponseEntity<>(
            new Garment("", "", "", "", "", "", 0, ""),
            HttpStatus.BAD_REQUEST
          );
      }
      imagesRepository.save(images);
      garment.setImages(images);
      garmentRepo.save(garment);
      System.out.println(garment);
    } catch (IOException e) {
      return new ResponseEntity<>(
        new Garment("", "", "", "", "", "", 0, ""),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
    return new ResponseEntity<>(garment, HttpStatus.CREATED);
  }

  public ResponseEntity<String> downloadLink(Long id, int imageNumber) {
    try {
      String region = "s3-sa-east-1";
      String amazon = "amazonaws.com";
      String fileName = "";

      Garment garment = garmentRepo.getOne(id);
      System.out.println(garment); // Imprimo el recurso para hacer saltar la excepcion de ID NO EXISTENTE

      Images images = garment.getImages();

      if (images == null) {
        return new ResponseEntity<>("No images found", HttpStatus.BAD_REQUEST);
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
          return new ResponseEntity<>(
            "Number of image does not found, range is 1 - 6",
            HttpStatus.BAD_REQUEST
          );
      }

      return new ResponseEntity<>(
        "https://" +
        bucketName +
        "." +
        region +
        "." +
        amazon +
        "/" +
        id.toString() +
        "/" +
        fileName,
        HttpStatus.OK
      );
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>("ID not found", HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Garment> deleteGarmentImage(Long id, int nroImage) {
    try {
      Garment garment = garmentRepo.getOne(id);
      if (garment.getId() == null) {
        return garmentNotFound;
      }

      Images images = garment.getImages();
      if (images.getId() == null) {
        return imagesNotFound;
      }

      String key;
      switch (nroImage) {
        case 1:
          key = id + "/" + images.getLinkImage1();
          break;
        case 2:
          key = id + "/" + images.getLinkImage2();
          break;
        case 3:
          key = id + "/" + images.getLinkImage3();
          break;
        case 4:
          key = id + "/" + images.getLinkImage4();
          break;
        case 5:
          key = id + "/" + images.getLinkImage5();
          break;
        case 6:
          key = id + "/" + images.getLinkImage6();
          break;
        default:
          return imagesNotFound;
      }

      boolean delete = s3FileStore.deleteObject(bucketName, key);
      if (delete) {
        switch (nroImage) {
          case 1:
            images.setLinkImage1(null);
            garmentRepo.save(garment);
            break;
          case 2:
            images.setLinkImage2(null);
            garmentRepo.save(garment);
            break;
          case 3:
            images.setLinkImage3(null);
            garmentRepo.save(garment);
            break;
          case 4:
            images.setLinkImage4(null);
            garmentRepo.save(garment);
            break;
          case 5:
            images.setLinkImage5(null);
            garmentRepo.save(garment);
            break;
          case 6:
            images.setLinkImage6(null);
            garmentRepo.save(garment);
            break;
          default:
            return imagesNotFound;
        }
        return new ResponseEntity<>(garment, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(garment, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (EntityNotFoundException entityNotFoundException) {
      System.out.println(entityNotFoundException.getMessage());
      return garmentNotFound;
    }
  }

  public ResponseEntity<Garment> deleteGarmentImages(Long id) {
    try {
      Garment garment = garmentRepo.getOne(id);
      if (garment.getId() == null) {
        return garmentNotFound;
      }

      Images images = garment.getImages();
      if (images == null) {
        System.out.println("No hay images!!!!!");
        return imagesNotFound;
      }
      System.out.println("No hay images y estoy aca afuera :|  ");
      ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
      if(images.getLinkImage1() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage1()));
      }
      if(images.getLinkImage2() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage2()));
      }
      if(images.getLinkImage3() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage3()));
      }
      if(images.getLinkImage4() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage4()));
      }
      if(images.getLinkImage5() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage5()));
      }
      if(images.getLinkImage6() != null) {
        keys.add(new DeleteObjectsRequest.KeyVersion(id.toString() +"/"+images.getLinkImage6()));
      }
      boolean delete = s3FileStore.deleteAllObject(bucketName,keys);

      if(delete) {
        return new ResponseEntity<>(garment, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(garment, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (EntityNotFoundException entityNotFoundException) {
      System.out.println(entityNotFoundException.getMessage());
      return garmentNotFound;
    }
  }

  public static String pathMaker(String filename,String id) {
    String bucketName = BucketName.IMAGE_BUCKET.getBucketName();
    String region = "s3-sa-east-1";
    String amazon = "amazonaws.com";
    String fileName = "";
    return "https://" +
            bucketName +
            "." +
            region +
            "." +
            amazon +
            "/" +
            id +
            "/" +
            filename;
  }
  public ResponseEntity<Images> downloadAllLink(Long id) {
    try {
      Garment garment = garmentRepo.getOne(id);
      System.out.println(garment.getId()); //Se imprime para hacer saltar la excepcion si no hay entidad con el id recibido.
      Images images = garment.getImages();

      if(images == null) {
        return new ResponseEntity<>(new Images(), HttpStatus.NOT_FOUND);
      }
      if(images.getLinkImage1() != null) {
        images.setLinkImage1(pathMaker(images.getLinkImage1(),id.toString()));
      }
      if(images.getLinkImage2() != null) {
        images.setLinkImage2(pathMaker(images.getLinkImage2(),id.toString()));
      }
      if(images.getLinkImage3() != null) {
        images.setLinkImage3(pathMaker(images.getLinkImage3(),id.toString()));
      }
      if(images.getLinkImage4() != null) {
        images.setLinkImage4(pathMaker(images.getLinkImage4(),id.toString()));
      }
      if(images.getLinkImage5() != null) {
        images.setLinkImage5(pathMaker(images.getLinkImage5(),id.toString()));
      }
      if(images.getLinkImage6() != null) {
        images.setLinkImage6(pathMaker(images.getLinkImage6(),id.toString()));
      }
      return new ResponseEntity<>(images,HttpStatus.OK);
    }
    catch (EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(new Images(), HttpStatus.NOT_FOUND);
    }
  }
}
