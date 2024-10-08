package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class APIError {
  private String Code;
  private String Message;
  private String Status;

  public String toString() {
    return "{Code='" + Code + "', Message=" + Message + "}";
  }

  // Default constructor
  public APIError() {}

  // Constructor with parameters
  @JsonCreator
  public APIError(
      @JsonProperty("code") String code,
      @JsonProperty("message") String message,
      @JsonProperty("status") String status) {
    this.Code = code;
    this.Message = message;
    this.Status = status;
  }

  // Getters
  public String getCode() {
    return Code;
  }

  public String getMessage() {
    return Message;
  }
}
