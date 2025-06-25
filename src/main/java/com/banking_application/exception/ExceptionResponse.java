package com.banking_application.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionResponse {

  @JsonProperty("statusCode")
  private final int statusCode;

  @JsonProperty("message")
  private final String  message;

  public ExceptionResponse(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode(){
    return statusCode;
  }
  public String getMessage() { return message; }
}
