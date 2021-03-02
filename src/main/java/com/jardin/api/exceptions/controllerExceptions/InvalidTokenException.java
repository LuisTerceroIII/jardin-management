package com.jardin.api.exceptions.controllerExceptions;



public class InvalidTokenException extends RuntimeException{
    private static final String detail = "Invalid Token";
    public InvalidTokenException() {
        super(detail);
    }
}
