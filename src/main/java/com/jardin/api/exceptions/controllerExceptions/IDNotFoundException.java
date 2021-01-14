package com.jardin.api.exceptions.controllerExceptions;

public class IDNotFoundException extends BadRequestException {

    private static final String DESCRIPTION = "This ID not exists";
    public IDNotFoundException(String detail) {
        super(DESCRIPTION + "  " + detail);
    }
}
