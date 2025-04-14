package io.getstream.client;

import static org.junit.Assert.*;

import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.models.*;
import io.getstream.core.options.EnrichmentFlags;
import java.util.Date;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

public class EnrichedActivityModerationIT {

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
    client =
        Client.builder(apiKey, secret)
            .httpClient(new OKHTTPClientAdapter(new OkHttpClient()))
            .build();
  }

  @Test
  public void testEnrichedActivityModerationFields() throws Exception {
    // Create a feed to work with
    FlatFeed feed = client.flatFeed("user", "test-moderation123");
    
    // Create an activity with moderation template and text that should trigger moderation
    Activity activity =
        Activity.builder()
            .actor("test-user")
            .verb("post")
            .object("test-object")
            .moderationTemplate("moderation_template_activity")
            .extraField("text", "good-text") // Using the term that triggers moderation in ModerationClientTest
            .foreignID("test-" + System.currentTimeMillis())
            .time(new Date())
            .build();

    // Add activity to feed
    Activity addedActivity = feed.addActivity(activity).join();
    assertNotNull(addedActivity);
    
    // Verify the Activity has moderation fields
    ModerationResponse activityModeration = addedActivity.getModerationResponse();
    assertNotNull(activityModeration);
    assertEquals("complete", activityModeration.getStatus());
    assertEquals("keep", activityModeration.getRecommendedAction());
    
    // Get enriched activities to verify moderation fields are included
    List<EnrichedActivity> enrichedActivities =
        feed.getEnrichedActivities(new EnrichmentFlags()).join();
    
    // Find our activity in the list
    EnrichedActivity enrichedActivity = null;
    for (EnrichedActivity ea : enrichedActivities) {
      if (ea.getID().equals(addedActivity.getID())) {
        enrichedActivity = ea;
        break;
      }
    }
    
    assertNotNull("Could not find the added activity in enriched activities", enrichedActivity);
    
    // Verify the EnrichedActivity has moderation fields
    ModerationResponse enrichedModeration = enrichedActivity.getModerationResponse();
    assertNotNull("EnrichedActivity should have moderation fields", enrichedModeration);
    assertEquals("complete", enrichedModeration.getStatus());
    assertEquals("keep", enrichedModeration.getRecommendedAction());
    assertEquals("moderation_template_activity", enrichedActivity.getModerationTemplate());
    
    // Clean up
    feed.removeActivityByID(addedActivity.getID()).join();
  }
} 