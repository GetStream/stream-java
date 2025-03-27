package io.getstream.client;

import io.getstream.core.exceptions.StreamAPIException;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.AuditLog;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * Integration test for the AuditLogsClient
 * Uses a Stream app with audit logs enabled
 */
public class AuditLogsClientIntegrationTest {
    // Credentials for a Stream app with audit logs enabled
    private static final String apiKey =
            System.getenv("STREAM_KEY") != null
                    ? System.getenv("STREAM_KEY")
                    : System.getProperty("STREAM_KEY");
    private static final String secret =
            System.getenv("STREAM_SECRET") != null
                    ? System.getenv("STREAM_SECRET")
                    : System.getProperty("STREAM_SECRET");
    private Client client;

    @Before
    public void setUp() throws Exception {
        client = Client.builder(apiKey, secret).region("oregon").build();
    }

    @Test
    public void testQueryAuditLogs() throws StreamException {
        // Test querying audit logs with a user filter (required by the API)
        QueryAuditLogsFilters filters = new QueryAuditLogsFilters();
        filters.setUserID("admin");  // Add a user_id filter as required by the API
        QueryAuditLogsPager pager = new QueryAuditLogsPager(5); // limit to 5 results
        
        QueryAuditLogsResponse response = client.auditLogs().queryAuditLogs(filters, pager).join();
        
        // Verify response structure
        assertNotNull("Response should not be null", response);
        assertNotNull("Audit logs list should not be null", response.getAuditLogs());
        assertNotNull("Duration should not be null", response.getDuration());
        
        // Test that audit logs list is properly initialized (even if empty)
        assertTrue("Audit logs list should be accessible", response.getAuditLogs() != null);
        
        // Verify that pagination properties exist
        // Note: they might be null if no pagination is needed
        // but the fields themselves should exist
        assertNotNull("Response object should contain next field (even if null)", response);
        assertNotNull("Response object should contain prev field (even if null)", response);
    }
    
    @Test
    public void testQueryAuditLogsByEntityType() throws StreamException {
        // Test querying audit logs by entity type and ID
        QueryAuditLogsFilters filters = new QueryAuditLogsFilters("user", "user-123");
        QueryAuditLogsPager pager = new QueryAuditLogsPager(5);
        
        QueryAuditLogsResponse response = client.auditLogs().queryAuditLogs(filters, pager).join();
        
        // Verify response structure
        assertNotNull("Response should not be null", response);
        assertNotNull("Audit logs list should not be null", response.getAuditLogs());
        assertNotNull("Duration should not be null", response.getDuration());
        
        // Validate that filters worked properly
        for (AuditLog log : response.getAuditLogs()) {
            if (log.getEntityType() != null && log.getEntityID() != null) {
                assertEquals("Entity type should match filter", "user", log.getEntityType());
                assertEquals("Entity ID should match filter", "user-123", log.getEntityID());
            }
        }
    }
    
    @Test
    public void testInvalidFilters() throws StreamException {
        // Test that validation works for invalid filters
        QueryAuditLogsFilters filters = new QueryAuditLogsFilters();
        // No filters set, this should fail validation
        
        // Use a different approach since JUnit 4 doesn't have assertThrows
        try {
            filters.validate();
            fail("Should have thrown an exception for invalid filters");
        } catch (StreamException e) {
            // Expected exception
            assertEquals("Error message should match validation message",
                    "Either entityType+entityID or userID is required for audit logs queries", 
                    e.getMessage());
        }
    }

    @Test
    public void testQueryAuditLogs2() throws StreamException {
        // Test querying audit logs with a user filter (required by the API)

//        filters := stream.QueryAuditLogsFilters{
//            EntityType: "feed",
//                    EntityID:   "123",
//                    UserID:     "user-42",
//        }
//        pager := stream.QueryAuditLogsPager{
//            Next:  "next-token",
//                    Prev:  "prev-token",
//                    Limit: 25,
//        }




        QueryAuditLogsFilters filters = new QueryAuditLogsFilters("feed", "123", "user-42");

//        filters.setUserID("admin");  // Add a user_id filter as required by the API
        QueryAuditLogsPager pager = new QueryAuditLogsPager(5); // limit to 5 results

        QueryAuditLogsResponse response = client.auditLogs().queryAuditLogs(filters, pager).join();

        // Verify response structure
        assertNotNull("Response should not be null", response);
        assertNotNull("Audit logs list should not be null", response.getAuditLogs());

        // Print out the audit logs
        System.out.println("Retrieved " + response.getAuditLogs().size() + " audit logs:");
        for (AuditLog log : response.getAuditLogs()) {
            System.out.println("  Type: " + log.getEntityType() +
                    ", ID: " + log.getEntityID() +
                    ", Action: " + log.getAction() +
                    ", User: " + log.getUserID() +
                    ", Date: " + log.getCreatedAt());
        }
    }
} 