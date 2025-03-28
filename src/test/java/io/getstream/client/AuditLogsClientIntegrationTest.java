package io.getstream.client;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        client = Client.builder(apiKey, secret).build();
    }


    @Test
    public void testQueryAuditLogs() throws StreamException {
        FlatFeed userFeed = client.flatFeed("user", "1");
        Activity a = Activity.builder().actor("userid:1").verb("tweet").object("Tweet:1").build();
        a = userFeed.addActivity(a).join();

        // Using the convenience method for activity entities
        QueryAuditLogsPager pager = new QueryAuditLogsPager(5); // limit to 5 results
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forActivity(a.getID());
        QueryAuditLogsResponse response = client.auditLogs().queryAuditLogs(filters, pager).join();

        // Verify response structure
        assertNotNull("Response should not be null", response);
        assertNotNull("Audit logs list should not be null", response.getAuditLogs());
        assertNotEquals(0, response.getAuditLogs().size());

        filters = QueryAuditLogsFilters.forUser("userid:1");
        response = client.auditLogs().queryAuditLogs(filters, pager).join();

        // Verify response structure
        assertNotNull("Response should not be null", response);
        assertNotNull("Audit logs list should not be null", response.getAuditLogs());
        assertNotEquals(0, response.getAuditLogs().size());
    }
}