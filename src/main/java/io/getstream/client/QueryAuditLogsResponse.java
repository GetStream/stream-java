package io.getstream.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.getstream.core.models.AuditLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryAuditLogsResponse {
    @JsonProperty("audit_logs")
    private List<AuditLog> auditLogs;
    
    private String next;
    private String prev;
    private String duration;
    
    // Default constructor
    public QueryAuditLogsResponse() {
        this.auditLogs = new ArrayList<>();
    }
    
    // Constructor with parameters
    @JsonCreator
    public QueryAuditLogsResponse(
            @JsonProperty("audit_logs") List<AuditLog> auditLogs,
            @JsonProperty("next") String next,
            @JsonProperty("prev") String prev,
            @JsonProperty("duration") String duration) {
        // Initialize mandatory fields with safe defaults if they're null
        this.auditLogs = auditLogs != null ? auditLogs : new ArrayList<>();
        this.next = next;
        this.prev = prev;
        this.duration = duration != null ? duration : "";
    }
    
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }
    
    public String getNext() {
        return next;
    }
    
    public String getPrev() {
        return prev;
    }
    
    public String getDuration() {
        return duration;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryAuditLogsResponse that = (QueryAuditLogsResponse) o;
        return Objects.equals(auditLogs, that.auditLogs) && 
               Objects.equals(next, that.next) && 
               Objects.equals(prev, that.prev) && 
               Objects.equals(duration, that.duration);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(auditLogs, next, prev, duration);
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("auditLogs", this.auditLogs)
                .add("next", this.next)
                .add("prev", this.prev)
                .add("duration", this.duration)
                .toString();
    }
} 