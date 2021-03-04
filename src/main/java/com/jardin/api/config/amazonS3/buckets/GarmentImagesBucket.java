package com.jardin.api.config.amazonS3.buckets;

public enum GarmentImagesBucket {
  IMAGE_BUCKET("jardin-products-images");

  private final String bucketName;

  GarmentImagesBucket(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getBucketName() {
    return bucketName;
  }
}
