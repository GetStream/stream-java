package io.getstream.core.models;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExportIDsResponse {
    @JsonProperty("export")
    private ExportIDsResult export;

    @JsonProperty("duration")
    private String duration;

    // No-argument constructor
    public ExportIDsResponse() {
    }

    // Constructor with parameters
    public ExportIDsResponse(String duration) {
        this.duration = duration;
    }

    public ExportIDsResult getExport() {
        return export;
    }

    public void setExport(ExportIDsResult export) {
        this.export = export;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}