package com.jardin.api.exceptions.controllerExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@RestControllerAdvice
public class ApiHandlerException extends ResponseEntityExceptionHandler {


    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorMessage> handleInvalidTokenException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.UNAUTHORIZED.value(),req.getRequestURI(), "UNAUTHORIZED"),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNoSuchElementFoundException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.NOT_FOUND.value(), req.getRequestURI(), "NOT FOUND"),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResourceListException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ErrorMessage> handleEmptyResourceListException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.NO_CONTENT.value(), req.getRequestURI(), "NOT CONTENT"),HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ResourceCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleResourceCreationException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getRequestURI(), "INTERNAL SERVER ERROR"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceDeleteException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleResourceDeleteException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getRequestURI(), "INTERNAL SERVER ERROR"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceUpdateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleResourceUpdateException(HttpServletRequest req, Exception e) {
        return new ResponseEntity<>(new ErrorMessage(e,HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getRequestURI(), "INTERNAL SERVER ERROR"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleAllUncaughtException(
            Exception exception,
            HttpServletRequest request){
        return new ResponseEntity<ErrorMessage>(new ErrorMessage(exception,HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI(), "INTERNAL SERVER ERROR"),HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
