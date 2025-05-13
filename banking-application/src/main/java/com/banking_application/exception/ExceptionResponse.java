package com.banking_application.exception;

public class ExceptionResponse extends RuntimeException {

  private final int port;

  public ExceptionResponse(int statusCode, String message) {
    super(message);
    this.port = statusCode;
  }

  public int getStatusCode(){
    return port;
  }
}
