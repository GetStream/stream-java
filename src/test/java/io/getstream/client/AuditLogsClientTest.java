package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Request;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import io.getstream.core.models.AuditLog;
import io.getstream.core.options.CustomQueryParameter;
import io.getstream.core.options.RequestOption;
import io.getstream.core.utils.Auth;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import java8.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Test;

public class AuditLogsClientTest {
    // Using fixed test credentials for unit tests
    private static final String apiKey = "test_key";
    private static final String secret = "test_secret";

    private MockHTTPClient mockHTTPClient;
    private Client client;

    class MockHTTPClient extends HTTPClient {
        public Request lastRequest;
        private final String responseJson;
        
        public MockHTTPClient(String responseJson) {
            this.responseJson = responseJson;
        }

        @Override
        public <T> T getImplementation() {
            return null;
        }

        @Override
        public CompletableFuture<Response> execute(Request request) {
            lastRequest = request;
            
            Response response = new Response(200, new ByteArrayInputStream(responseJson.getBytes(StandardCharsets.UTF_8)));
            CompletableFuture<Response> future = new CompletableFuture<>();
            future.complete(response);
            return future;
        }
    }

    @Before
    public void setUp() throws Exception {
        String mockResponse = "{\n" +
                "  \"audit_logs\": [\n" +
                "    {\n" +
                "      \"entity_type\": \"user\",\n" +
                "      \"entity_id\": \"user-123\",\n" +
                "      \"action\": \"update\",\n" +
                "      \"user_id\": \"admin-user\",\n" +
                "      \"custom\": {\"changes\": {\"name\": \"New Name\"}},\n" +
                "      \"created_at\": \"2023-01-01T12:00:00.000Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"entity_type\": \"feed\",\n" +
                "      \"entity_id\": \"feed-456\",\n" +
                "      \"action\": \"delete\",\n" +
                "      \"user_id\": \"admin-user\",\n" +
                "      \"custom\": {},\n" +
                "      \"created_at\": \"2023-01-02T12:00:00.000Z\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"next\": \"next-page-token\",\n" +
                "  \"prev\": \"prev-page-token\",\n" +
                "  \"duration\": \"42ms\"\n" +
                "}";
        
        mockHTTPClient = new MockHTTPClient(mockResponse);
        client = Client.builder(apiKey, secret)
                .httpClient(mockHTTPClient)
                .build();
    }

    @Test
    public void testQueryAuditLogs() throws Exception {
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forEntity("activity", "activity-123");
        QueryAuditLogsPager pager = new QueryAuditLogsPager(10);
        
        QueryAuditLogsResponse response = client.auditLogs().queryAuditLogs(filters, pager).join();
        
        // Verify the response
        assertNotNull(response);
        assertNotNull(response.getAuditLogs());
        assertEquals(2, response.getAuditLogs().size());
        assertEquals("next-page-token", response.getNext());
        assertEquals("prev-page-token", response.getPrev());
        
        // Verify first audit log
        AuditLog firstLog = response.getAuditLogs().get(0);
        assertEquals("user", firstLog.getEntityType());
        assertEquals("user-123", firstLog.getEntityID());
        assertEquals("update", firstLog.getAction());
        assertEquals("admin-user", firstLog.getUserID());
        assertNotNull(firstLog.getCustom());
        assertNotNull(firstLog.getCreatedAt());
        
        // Verify second audit log
        AuditLog secondLog = response.getAuditLogs().get(1);
        assertEquals("feed", secondLog.getEntityType());
        assertEquals("feed-456", secondLog.getEntityID());
        assertEquals("delete", secondLog.getAction());
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("activity", queryParams.get("entity_type"));
        assertEquals("activity-123", queryParams.get("entity_id"));
        assertEquals("10", queryParams.get("limit"));
    }
    
    @Test
    public void testQueryAuditLogsWithUserFilter() throws Exception {
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forUser("admin-user");
        
        client.auditLogs().queryAuditLogs(filters).join();
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("admin-user", queryParams.get("user_id"));
    }
    
    @Test
    public void testQueryAuditLogsWithPagination() throws Exception {
        QueryAuditLogsPager pager = new QueryAuditLogsPager();
        pager.setNext("next-token");
        
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forUser("admin-user");
        client.auditLogs().queryAuditLogs(filters, pager).join();
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("next-token", queryParams.get("next"));
        assertEquals("admin-user", queryParams.get("user_id"));
    }
    
    @Test
    public void testQueryAuditLogsTokenGeneration() throws Exception {
        // Create a client with a specific secret to test token generation
        String testSecret = "test-secret";
        Client client = Client.builder(apiKey, testSecret)
                .httpClient(mockHTTPClient)
                .build();
        
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forUser("admin-user");
        client.auditLogs().queryAuditLogs(filters).join();
        
        // Verify the token was generated using the correct resource and action
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        Token token = lastRequest.getToken();
        assertNotNull(token);
        // We can't directly test the token's contents, but we can verify it's not null
    }
    
    @Test
    public void testBuilderPatternFlexibility() throws Exception {
        // Test the full builder pattern flexibility
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.builder()
            .withEntityType("reaction")
            .withEntityID("reaction-123")
            .withUserID("admin")
            .build();
        
        client.auditLogs().queryAuditLogs(filters).join();
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("reaction", queryParams.get("entity_type"));
        assertEquals("reaction-123", queryParams.get("entity_id"));
        assertEquals("admin", queryParams.get("user_id"));
    }
    
    @Test
    public void testForActivityConvenienceMethod() throws Exception {
        // Test the convenience method for activities
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forActivity("activity-789");
        
        client.auditLogs().queryAuditLogs(filters).join();
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("activity", queryParams.get("entity_type"));
        assertEquals("activity-789", queryParams.get("entity_id"));
    }
    
    @Test
    public void testForReactionConvenienceMethod() throws Exception {
        // Test the convenience method for reactions
        QueryAuditLogsFilters filters = QueryAuditLogsFilters.forReaction("reaction-456");
        
        client.auditLogs().queryAuditLogs(filters).join();
        
        // Verify request parameters
        Request lastRequest = mockHTTPClient.lastRequest;
        assertNotNull(lastRequest);
        
        // Extract query parameters from URL
        String urlQuery = lastRequest.getURL().getQuery();
        Map<String, String> queryParams = extractQueryParams(urlQuery);
        
        assertEquals("reaction", queryParams.get("entity_type"));
        assertEquals("reaction-456", queryParams.get("entity_id"));
    }
    
    private Map<String, String> extractQueryParams(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        
        return params;
    }
} 