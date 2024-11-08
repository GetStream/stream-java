package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.models.Activity;
import io.getstream.core.models.ExportIDsResponse;
import io.getstream.core.models.ExportIDsResult;
import org.junit.*;

import java.util.Date;

public class ExportIDsTest {

    private static final String apiKey =
            System.getenv("STREAM_KEY") != null
                    ? System.getenv("STREAM_KEY")
                    : System.getProperty("STREAM_KEY");
    private static final String secret =
            System.getenv("STREAM_SECRET") != null
                    ? System.getenv("STREAM_SECRET")
                    : System.getProperty("STREAM_SECRET");

    Client client;

    @Before
    public void setUp() throws Exception {
        client = Client.builder(apiKey, secret).build();
    }

    @Test
    public void testExportUserActivities() throws Exception {

        String userId = "test-user";

        // Insert some activities
        Activity activity1 = Activity.builder()
                .actor(userId)
                .verb("post")
                .object("object1")
                .time(new Date())
                .build();
        Activity activity1Res = client.flatFeed("user", userId).addActivity(activity1).join();

        Activity activity2 = Activity.builder()
                .actor(userId)
                .verb("like")
                .object("object2")
                .time(new Date())
                .build();
        Activity activity2Res = client.flatFeed("user", userId).addActivity(activity2).join();

        // Export user activities
        ExportIDsResponse exportResult = client.exportUserActivities(userId).join();
        ExportIDsResult exports = exportResult.getExport();

        // Test the output
        assertNotNull(exportResult);
        assertEquals(userId, exports.getUserId());
        assertTrue(exports.getActivityCount() >= 0);
        assertNotNull(exports.getActivityIds());
        assertTrue(exports.getActivityIds().contains(activity1Res.getID()));
        assertTrue(exports.getActivityIds().contains(activity2Res.getID()));
        assertTrue(exports.getReactionCount() >= 0);
        assertNotNull(exports.getReactionIds());
    }
}