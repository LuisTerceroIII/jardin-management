package com.jardin.api.exceptions.controllers;

public class ResourceUpdateException extends RuntimeException {
    private static final String detail = "Fail to update resource : ";

    public ResourceUpdateException(String resource, Long id) {
        super(detail + resource + "- ID : " + id);
    }
}
