package com.jardin.api.exceptions.controllerExceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ErrorMessage {
  private final String exception;
  private final String message;
  private final String path;
  private final int status;
  private final String error;

  public ErrorMessage(Exception exception, int status, String path, String error) {
    this.exception = exception.getClass().getSimpleName();
    this.status = status;
    this.message = exception.getMessage();
    this.path = path;
    this.error = error;
  }

  public String getException() {
    return exception;
  }

  public String getMessage() {
    return message;
  }

  public String getError() {
    return error;
  }

  public int getStatus() {
    return status;
  }

  public String getPath() {
    return path;
  }



  @Override
  public String toString() {
    return "ErrorMessage{" +
            "exception='" + exception + '\'' +
            ", message='" + message + '\'' +
            ", error='" + error + '\'' +
            ", status='" + status + '\'' +
            ", path='" + path + '\'' +
            '}';
  }
}
