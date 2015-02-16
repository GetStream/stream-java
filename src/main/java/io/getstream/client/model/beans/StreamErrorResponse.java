package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Map the response message coming from GetStream.io server.
 */
public class StreamErrorResponse {

	private int code;
	private String detail;
	private String duration;
	private String exception;

	@JsonProperty("status_code")
	private int statusCode;

	public int getCode() {
		return code;
	}

	public String getDetail() {
		return detail;
	}

	public String getDuration() {
		return duration;
	}

	public String getException() {
		return exception;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
