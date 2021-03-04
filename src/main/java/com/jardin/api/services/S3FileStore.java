package com.jardin.api.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.jardin.api.repositories.GarmentRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3FileStore {

  private final AmazonS3 s3;

  private final GarmentRepository garmentRepo;

  @Autowired
  public S3FileStore(AmazonS3 s3, GarmentRepository garmentRepo) {
    this.s3 = s3;
    this.garmentRepo = garmentRepo;
  }

  public void save(
    String path,
    String filename,
    Optional<Map<String, String>> objectMetadata,
    InputStream inputStream
  ) {
    ObjectMetadata metadata = new ObjectMetadata();
    objectMetadata.ifPresent(
      map -> {
        if (!map.isEmpty()) {
          map.forEach(metadata::addUserMetadata);
        }
      }
    );

    try {
      s3.putObject(path, filename, inputStream, metadata); //Aca enviamos el archivo a nuestro bucket
    } catch (AmazonServiceException e) {
      throw new IllegalStateException("Failed to store file in s3 bucket", e);
    }
  }

  public byte[] download(String path, String key) {
    try {
      S3Object object = s3.getObject(path, key);
      return IOUtils.toByteArray(object.getObjectContent());
    } catch (AmazonServiceException | IOException e) {
      throw new IllegalStateException("Failed to download file to s3", e);
    }
  }

  public boolean deleteObject(String bucketName, String key) {
    try {
      s3.deleteObject(new DeleteObjectRequest(bucketName, key));
      return true;
    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
      return false;
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      e.printStackTrace();
      return false;
    }
  }
  public boolean deleteAllObject(String bucketName, ArrayList<DeleteObjectsRequest.KeyVersion> keys) {
    try {

      DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName)
              .withKeys(keys)
              .withQuiet(false);

      // Verify that the objects were deleted successfully.
      DeleteObjectsResult delObjRes = s3.deleteObjects(multiObjectDeleteRequest);
      int successfulDeletes = delObjRes.getDeletedObjects().size();
      System.out.println(successfulDeletes + " objects successfully deleted.");
      return true;

    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
      return false;
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      e.printStackTrace();
      return false;
    }
  }

}
