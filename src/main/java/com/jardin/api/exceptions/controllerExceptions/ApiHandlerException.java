package com.jardin.api.exceptions.controllerExceptions;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class ApiHandlerException {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(
    {
      BadRequestException.class,
      javax.persistence.EntityNotFoundException.class,
    }
  )
  @ResponseBody
  public ErrorMessage badRequest(
    HttpServletRequest request,
    Exception exception
  ) {
    return new ErrorMessage(exception, request.getRequestURI());
  }
}
