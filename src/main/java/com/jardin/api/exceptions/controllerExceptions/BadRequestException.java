package com.jardin.api.exceptions.controllerExceptions;

public class BadRequestException extends RuntimeException {

  private static final String DESCRIPTION = "Bad request Exception 400";

  public BadRequestException(String detail) {
    super(DESCRIPTION + " " + detail);
  }
}
