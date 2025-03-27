package io.getstream.client;

import io.getstream.core.exceptions.StreamException;

/**
 * Filters for querying audit logs.
 * Either entityType+entityID pair OR userID is required by the API.
 */
public class QueryAuditLogsFilters {
    private String entityType;
    private String entityID;
    private String userID;
    
    /**
     * Default constructor.
     * Note: You must set either (entityType AND entityID) OR userID before using.
     */
    public QueryAuditLogsFilters() {
    }
    
    /**
     * Constructor with entity type and ID.
     * 
     * @param entityType The type of entity (e.g., "user", "feed")
     * @param entityID The ID of the entity
     */
    public QueryAuditLogsFilters(String entityType, String entityID) {
        this.entityType = entityType;
        this.entityID = entityID;
    }
    
    /**
     * Constructor with entity type, entity ID, and user ID.
     * 
     * @param entityType The type of entity (e.g., "user", "feed")
     * @param entityID The ID of the entity
     * @param userID The ID of the user
     */
    public QueryAuditLogsFilters(String entityType, String entityID, String userID) {
        this.entityType = entityType;
        this.entityID = entityID;
        this.userID = userID;
    }
    
    /**
     * Constructor with user ID only.
     * 
     * @param userID The ID of the user
     */
    public QueryAuditLogsFilters(String userID) {
        this.userID = userID;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getEntityID() {
        return entityID;
    }
    
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    /**
     * Validates that the filters contain the required fields.
     * Either (entityType AND entityID) OR userID must be set.
     * 
     * @throws StreamException if the required fields are not set
     */
    public void validate() throws StreamException {
        boolean hasEntityFields = entityType != null && !entityType.isEmpty() && 
                                 entityID != null && !entityID.isEmpty();
        boolean hasUserID = userID != null && !userID.isEmpty();
        
        if (!hasEntityFields && !hasUserID) {
            throw new StreamException("Either entityType+entityID or userID is required for audit logs queries");
        }
    }
    
    /**
     * Checks if the filter is valid according to API requirements.
     * 
     * @return true if either (entityType AND entityID) OR userID is set
     */
    public boolean isValid() {
        boolean hasEntityFields = entityType != null && !entityType.isEmpty() && 
                                 entityID != null && !entityID.isEmpty();
        boolean hasUserID = userID != null && !userID.isEmpty();
        
        return hasEntityFields || hasUserID;
    }
} 