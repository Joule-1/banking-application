package com.banking_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleCheckedException(Exception e){
        return new ResponseEntity<>(new ExceptionResponse(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleUncheckedException(ApiException e){
            return new ResponseEntity<>(new ExceptionResponse(e.getStatusCode(), e.getMessage()), HttpStatusCode.valueOf(e.getStatusCode()));
    }



}
