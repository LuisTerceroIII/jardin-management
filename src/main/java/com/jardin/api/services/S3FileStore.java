package com.jardin.api.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.jardin.api.buckets.BucketName;
import com.jardin.api.model.entities.Garment;
import com.jardin.api.model.entities.Images;
import com.jardin.api.repositories.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class S3FileStore {

    private final AmazonS3 s3;

    private final GarmentRepository garmentRepo;

    @Autowired
    public S3FileStore(AmazonS3 s3, GarmentRepository garmentRepo) {
        this.s3 = s3;
        this.garmentRepo = garmentRepo;
    }

    public void save(String path, String filename,
                     Optional<Map<String,String>> objectMetadata,
                     InputStream inputStream) {

        ObjectMetadata metadata = new ObjectMetadata();
        objectMetadata.ifPresent( map -> {
            if(!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try {
            s3.putObject(path,filename,inputStream,metadata); //Aca enviamos el archivo a nuestro bucket
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file in s3 bucket",e);
        }
    }

    public byte[] download(String path,String key) {
        try {
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download file to s3", e);
        }
    }

}
