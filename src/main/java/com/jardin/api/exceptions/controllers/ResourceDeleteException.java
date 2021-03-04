package com.jardin.api.exceptions.controllers;

public class ResourceDeleteException extends RuntimeException {
    private static final String detail = "Fail to delete resource : ";

    public ResourceDeleteException(String resource, Long id) {
        super(detail + resource + "- ID : " + id);
    }
}
