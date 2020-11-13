package com.jardin.api.buckets;

public enum BucketName {

    IMAGE_BUCKET("jardin-products-images");

    private final String bucketName;

     BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
