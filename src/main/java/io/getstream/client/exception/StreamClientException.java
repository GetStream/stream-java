package io.getstream.client.exception;

public class StreamClientException extends Exception {
    public StreamClientException() {
        super();
    }

    public StreamClientException(String message) {
        super(message);
    }

    public StreamClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamClientException(Throwable cause) {
        super(cause);
    }

    protected StreamClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
