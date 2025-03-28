package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;

public class AuditLog {
    @JsonProperty("entity_type")
    private String entityType;
    
    @JsonProperty("entity_id")
    private String entityID;
    
    private String action;
    
    @JsonProperty("user_id")
    private String userID;
    
    private Map<String, Object> custom;
    
    @JsonProperty("created_at")
    private Date createdAt;
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getEntityID() {
        return entityID;
    }
    
    public String getAction() {
        return action;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public Map<String, Object> getCustom() {
        return custom;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
} 