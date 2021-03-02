package com.jardin.api.exceptions.controllerExceptions;



public class NoSuchElementFoundException extends RuntimeException {

  private static final String DESCRIPTION = "Element id not found : ";

  public NoSuchElementFoundException(String id) {
    super(DESCRIPTION + id );
  }
}
