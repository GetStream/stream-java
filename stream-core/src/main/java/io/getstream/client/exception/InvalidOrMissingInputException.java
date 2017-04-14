package io.getstream.client.exception;

/**
 * In case the authentication failed on remote side (http status code: 401).
 */
public class InvalidOrMissingInputException extends StreamClientException {

    public InvalidOrMissingInputException() {
        super();
    }

    public InvalidOrMissingInputException(String message) {
        super(message);
    }

    public InvalidOrMissingInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOrMissingInputException(Throwable cause) {
        super(cause);
    }

    protected InvalidOrMissingInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}