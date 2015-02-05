package io.getstream.client.exception;

public abstract class StreamClientException extends Exception {

    private int httpStatusCode;
    private int code;
    private String exceptionField;
    private String detail;

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

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExceptionField() {
        return exceptionField;
    }

    public void setExceptionField(String exceptionField) {
        this.exceptionField = exceptionField;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
