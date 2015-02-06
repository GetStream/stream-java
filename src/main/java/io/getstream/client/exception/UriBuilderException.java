package io.getstream.client.exception;

public class UriBuilderException extends RuntimeException {
	public UriBuilderException() {
		super();
	}

	public UriBuilderException(String message) {
		super(message);
	}

	public UriBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public UriBuilderException(Throwable cause) {
		super(cause);
	}

	protected UriBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
