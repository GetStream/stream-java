package io.getstream.client.model.beans;

/**
 * Wrapper object for the response coming from the Meta endpoint.
 */
public class MetaResponse {

    private double duration;

    private int responseCode;

    public MetaResponse() {

    }

    public MetaResponse(double duration, int responseCode) {
        this.duration = duration;
        this.responseCode = responseCode;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Return the HTTP response code from the previous call.
     * @return HTTP response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
