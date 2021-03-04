package com.jardin.api.exceptions.controllers;

public class ResourceCreationException extends RuntimeException{
    private static final String detail = "Fail to create resource : ";

    public ResourceCreationException(String resource) {
        super(detail + resource);
    }
}
