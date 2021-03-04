package com.jardin.api.exceptions.token;



public class InvalidTokenException extends RuntimeException{
    private static final String detail = "Invalid Token";
    public InvalidTokenException() {
        super(detail);
    }
}
