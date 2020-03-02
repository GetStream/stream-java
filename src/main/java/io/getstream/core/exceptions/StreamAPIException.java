package io.getstream.core.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StreamAPIException extends StreamException {
  private final int errorCode;
  private final int statusCode;
  private final String errorName;

  @JsonCreator
  public StreamAPIException(
      @JsonProperty("detail") String message,
      @JsonProperty("code") int errorCode,
      @JsonProperty("status_code") int statusCode,
      @JsonProperty("exception") String errorName) {
    super(formatMessage(message, errorName, errorCode, statusCode));

    this.errorCode = errorCode;
    this.statusCode = statusCode;
    this.errorName = errorName;
  }

  private static String formatMessage(
      String message, String errorName, int errorCode, int statusCode) {
    StringBuilder result = new StringBuilder();
    if (errorName != null && !errorName.isEmpty()) {
      result.append(errorName);
    }
    if (message != null && !message.isEmpty()) {
      if (result.length() > 0) {
        result.append(": ");
      }

      result.append(message);
    }
    if (result.length() > 0) {
      result.append(" ");
    }
    result.append("(code = ");
    result.append(errorCode);
    result.append(" status = ");
    result.append(statusCode);
    result.append(')');
    return result.toString();
  }

  public int getErrorCode() {
    return errorCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getErrorName() {
    return errorName;
  }
}
