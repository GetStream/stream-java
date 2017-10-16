package io.getstream.client.exception;

/**
 * In case you have exceed your allowed calls per time interval (i.e. 1min, 15min or 1hr)
 */
public class RateLimitExceededException extends StreamClientException {

    public RateLimitExceededException() {
        super();
    }

    public RateLimitExceededException(String message) {
        super(message);
    }

    public RateLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitExceededException(Throwable cause) {
        super(cause);
    }

    protected RateLimitExceededException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
