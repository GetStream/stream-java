package io.getstream.client.exception;

/**
 * Exception returned in case the feed slug or the feed id are
 * not valid.ß
 */
public class InvalidFeedNameException extends StreamClientException {

    public InvalidFeedNameException() {
    }

    public InvalidFeedNameException(String message) {
        super(message);
    }

    public InvalidFeedNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFeedNameException(Throwable cause) {
        super(cause);
    }

    public InvalidFeedNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
