package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

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

    @JsonProperty("exception_fields")
    private JsonNode exceptionFields;

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

    @JsonRawValue
    public String getExceptionFields() {
        return exceptionFields == null ? null : exceptionFields.toString();
    }

    public void setExceptionFields(JsonNode exceptionFields) {
        this.exceptionFields = exceptionFields;
    }
}
