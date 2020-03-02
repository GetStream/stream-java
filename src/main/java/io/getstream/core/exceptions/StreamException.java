package io.getstream.core.exceptions;

public class StreamException extends Exception {
  public StreamException() {
    super();
  }

  public StreamException(String message) {
    super(message);
  }

  public StreamException(String message, Throwable cause) {
    super(message, cause);
  }

  public StreamException(Throwable cause) {
    super(cause);
  }

  protected StreamException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
