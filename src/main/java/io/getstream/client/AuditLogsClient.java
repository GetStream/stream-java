package io.getstream.client;

import io.getstream.core.Stream;
import io.getstream.core.http.Token;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.AuditLog;
import io.getstream.core.options.RequestOption;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.utils.Auth.TokenAction;
import java8.util.concurrent.CompletableFuture;

import java.util.ArrayList;
import java.util.List;

import static io.getstream.core.utils.Auth.buildAuditLogsToken;

/**
 * Client for querying Stream audit logs.
 * Audit logs record changes to various entities within your Stream app.
 */
public final class AuditLogsClient {
    private final String secret;
    private final Stream stream;

    public AuditLogsClient(String secret, Stream stream) {
        this.secret = secret;
        this.stream = stream;
    }

    /**
     * Query audit logs with the specified filters and default pagination.
     *
     * @param filters Filters to apply to the query (either entityType+entityID OR userID is required)
     * @return CompletableFuture with the query response
     * @throws StreamException if the filters are invalid or if there's an API error
     */
    public CompletableFuture<QueryAuditLogsResponse> queryAuditLogs(QueryAuditLogsFilters filters) throws StreamException {
        return queryAuditLogs(filters, new QueryAuditLogsPager());
    }

    /**
     * Query audit logs with the specified filters and pagination.
     *
     * @param filters Filters to apply to the query (either entityType+entityID OR userID is required)
     * @param pager Pagination settings for the query
     * @return CompletableFuture with the query response
     * @throws StreamException if the filters are invalid or if there's an API error
     */
    public CompletableFuture<QueryAuditLogsResponse> queryAuditLogs(QueryAuditLogsFilters filters, QueryAuditLogsPager pager) throws StreamException {
        // Validate filters before making the API call
        if (filters == null) {
            throw new StreamException("Filters cannot be null for audit logs queries");
        }
        
        final Token token = buildAuditLogsToken(secret, TokenAction.READ);
        
        RequestOption[] options = buildRequestOptions(filters, pager);
        return stream.queryAuditLogs(token, options);
    }
    
    /**
     * Builds request options from filters and pagination settings.
     *
     * @param filters Filters to apply to the query
     * @param pager Pagination settings
     * @return Array of RequestOption for the API call
     */
    private RequestOption[] buildRequestOptions(QueryAuditLogsFilters filters, QueryAuditLogsPager pager) {
        List<RequestOption> options = new ArrayList<>();
        
        if (filters.getEntityType() != null && !filters.getEntityType().isEmpty() && 
            filters.getEntityID() != null && !filters.getEntityID().isEmpty()) {
            options.add(new CustomQueryParameter("entity_type", filters.getEntityType()));
            options.add(new CustomQueryParameter("entity_id", filters.getEntityID()));
        }
        
        if (filters.getUserID() != null && !filters.getUserID().isEmpty()) {
            options.add(new CustomQueryParameter("user_id", filters.getUserID()));
        }
        
        if (pager != null) {
            if (pager.getNext() != null && !pager.getNext().isEmpty()) {
                options.add(new CustomQueryParameter("next", pager.getNext()));
            }
            
            if (pager.getPrev() != null && !pager.getPrev().isEmpty()) {
                options.add(new CustomQueryParameter("prev", pager.getPrev()));
            }
            
            if (pager.getLimit() > 0) {
                options.add(new CustomQueryParameter("limit", Integer.toString(pager.getLimit())));
            }
        }
        
        return options.toArray(new RequestOption[0]);
    }
} 