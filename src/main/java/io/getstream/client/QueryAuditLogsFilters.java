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
     * Private constructor, use builder instead.
     */
    private QueryAuditLogsFilters() {
    }
    
    /**
     * Creates a new builder for QueryAuditLogsFilters.
     * 
     * @return a new QueryAuditLogsFilters.Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Creates a new filter for user ID queries.
     * 
     * @param userID The ID of the user
     * @return a new QueryAuditLogsFilters with the user ID set
     */
    public static QueryAuditLogsFilters forUser(String userID) {
        return builder().withUserID(userID).build();
    }
    
    /**
     * Creates a new filter for entity type and ID queries.
     * 
     * @param entityType The type of entity (e.g., "user", "feed")
     * @param entityID The ID of the entity
     * @return a new QueryAuditLogsFilters with the entity type and ID set
     */
    public static QueryAuditLogsFilters forEntity(String entityType, String entityID) {
        return builder().withEntityType(entityType).withEntityID(entityID).build();
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public String getEntityID() {
        return entityID;
    }
    
    public String getUserID() {
        return userID;
    }
    
    /**
     * Set the entity type for existing filter instance.
     * 
     * @param entityType The type of entity
     * @return this instance for method chaining
     */
    public QueryAuditLogsFilters setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    
    /**
     * Set the entity ID for existing filter instance.
     * 
     * @param entityID The ID of the entity
     * @return this instance for method chaining
     */
    public QueryAuditLogsFilters setEntityID(String entityID) {
        this.entityID = entityID;
        return this;
    }
    
    /**
     * Set the user ID for existing filter instance.
     * 
     * @param userID The ID of the user
     * @return this instance for method chaining
     */
    public QueryAuditLogsFilters setUserID(String userID) {
        this.userID = userID;
        return this;
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
    
    /**
     * Builder class for QueryAuditLogsFilters.
     */
    public static class Builder {
        private final QueryAuditLogsFilters filters;
        
        private Builder() {
            filters = new QueryAuditLogsFilters();
        }
        
        /**
         * Set the entity type.
         * 
         * @param entityType The type of entity (e.g., "user", "feed")
         * @return this builder for method chaining
         */
        public Builder withEntityType(String entityType) {
            filters.entityType = entityType;
            return this;
        }
        
        /**
         * Set the entity ID.
         * 
         * @param entityID The ID of the entity
         * @return this builder for method chaining
         */
        public Builder withEntityID(String entityID) {
            filters.entityID = entityID;
            return this;
        }
        
        /**
         * Set the user ID.
         * 
         * @param userID The ID of the user
         * @return this builder for method chaining
         */
        public Builder withUserID(String userID) {
            filters.userID = userID;
            return this;
        }
        
        /**
         * Builds the QueryAuditLogsFilters instance.
         * 
         * @return a new QueryAuditLogsFilters instance
         */
        public QueryAuditLogsFilters build() {
            return filters;
        }
    }
} 