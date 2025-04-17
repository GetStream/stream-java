package io.getstream.client;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import io.getstream.core.models.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class ModerationTemplateUpdateTest {

  // Using the provided API credentials
  private static final String apiKey = "mfajp2wqsk9e";
  private static final String secret = "9kve7vf7jdyqqxnqfqq8beq7vqvhk8z526kvqj8nd6uaenj6cns3v2psdnwucm78";

  private Client client;

  @Before
  public void setUp() throws Exception {
    client = Client.builder(apiKey, secret).build();
  }

  @Test
  public void testUpdateWithDifferentModerationTemplate() throws Exception {
    // Create an activity with an initial moderation template
    String foreignID = "mod-template-switch-test-" + System.currentTimeMillis();
    Activity activity =
        Activity.builder()
            .actor("stream-test-user")
            .verb("post")
            .object("test-object")
            .foreignID(foreignID)
            .time(new Date())
//            .moderationTemplate("activity-feed-mod-temp") // Initial template
            .moderationTemplate("reaction-template") // Initial template
//            .extraField("message", "fuck off")//bad text
            .extraField("message", "fuckdoff")//good text
            .build();
    
    // Add the activity to a test feed
    FlatFeed feed = client.flatFeed("user", "test-user");
    Activity createdActivity = feed.addActivity(activity).join();
    assertNotNull(createdActivity);
    
    // Get the initial moderation state
//    ModerationResponse initialModeration = createdActivity.getModerationResponse();
//    System.out.println("Initial moderation template: " + createdActivity.getModerationTemplate());
//    if (initialModeration != null) {
//      System.out.println("Initial moderation status: " + initialModeration.getStatus());
//      System.out.println("Initial moderation action: " + initialModeration.getRecommendedAction());
//    }
    
    try {
      // Now update the activity with a different moderation template
      // Also add a problematic text to trigger moderation
      Map<String, Object> set = new ImmutableMap.Builder<String, Object>()
          .put("message", "fuckffoff") // Text that should trigger moderation
          .put("moderation_template", "activity-feed-mod-temp") // Using a different template name
          .build();
      
      // Perform partial update by foreign ID
//      Activity updatedActivity = client
//          .updateActivityByForeignID(//mod-template-switch-test-1744901529170
//              new ForeignIDTimePair(foreignID, createdActivity.getTime()),
//              set,
//              Collections.emptyList())
//          .join();
      Activity updatedActivity = client
              .updateActivityByID(//mod-template-switch-test-1744901529170
                       createdActivity.getID(),
                      set,
                      Collections.emptyList())
              .join();

      // Verify the update worked
      assertNotNull(updatedActivity);
      assertEquals(foreignID, updatedActivity.getForeignID());
//      assertEquals("pissoar", updatedActivity.getExtra().get("text"));
      
      // Check if moderation template was updated
      System.out.println("Updated moderation template: " + updatedActivity.getModerationTemplate());

      // Check if moderation response came back with the update
      ModerationResponse updateModeration = updatedActivity.getModerationResponse();
      if (updateModeration != null) {
        System.out.println("Update response moderation status: " + updateModeration.getStatus());
        System.out.println("Update response moderation action: " + updateModeration.getRecommendedAction());
      }
      
      // Wait a moment for moderation to complete
      Thread.sleep(1000);
      
      // Get the full activity after update to check moderation
      List<Activity> activities = feed.getActivities().join();
      Activity fetchedActivity = null;
      for (Activity act : activities) {
        if (foreignID.equals(act.getForeignID())) {
          fetchedActivity = act;
          break;
        }
      }
      
      assertNotNull("Could not find the updated activity in feed", fetchedActivity);
      
      // Check if moderation template was updated
      System.out.println("Fetched moderation template: " + fetchedActivity.getModerationTemplate());
      
      // Check if moderation was applied
      ModerationResponse moderationResponse = fetchedActivity.getModerationResponse();
      if (moderationResponse != null) {
        assertNotNull("Moderation status should not be null", moderationResponse.getStatus());
        assertNotNull("Recommended action should not be null", moderationResponse.getRecommendedAction());
        System.out.println("Fetched moderation status: " + moderationResponse.getStatus());
        System.out.println("Fetched recommended action: " + moderationResponse.getRecommendedAction());
      }
    } finally {
      // Clean up
      feed.removeActivityByForeignID(foreignID).join();
    }
  }
  
  @Test
  public void testBatchUpdateWithDifferentModerationTemplate() throws Exception {
    // Create an activity with an initial moderation template
    String foreignID = "batch-template-switch-test-" + System.currentTimeMillis();
    Activity activity =
        Activity.builder()
            .actor("test-user")
            .verb("post")
            .object("test-object")
            .foreignID(foreignID)
            .time(new Date())
            .moderationTemplate("moderation_template_activity") // Initial template
            .extraField("text", "This is a safe text")
            .build();
    
    // Add the activity to a test feed
    FlatFeed feed = client.flatFeed("user", "test-user");
    Activity createdActivity = feed.addActivity(activity).join();
    assertNotNull(createdActivity);
    
    try {
      // Create an update that changes the moderation template
      ActivityUpdate update = ActivityUpdate.builder()
          .foreignID(foreignID)
          .time(createdActivity.getTime())
          .set(ImmutableMap.of(
              "text", "pissoar",
              "moderation_template", "different_moderation_template")) // Using a different template name
          .unset(Collections.emptyList())
          .build();
      
      try {
        // Perform the batch update
        List<Activity> updatedActivities = client.updateActivitiesByForeignID(update).join();
        assertNotNull(updatedActivities);
        assertFalse(updatedActivities.isEmpty());
        
        // Get the updated activity
        Activity updatedActivity = updatedActivities.get(0);
        assertEquals(foreignID, updatedActivity.getForeignID());
        
        // Check if moderation template was updated in response
        if (updatedActivity.getModerationTemplate() != null) {
          System.out.println("Batch updated moderation template: " + updatedActivity.getModerationTemplate());
        }
        
        // Check moderation if it's there
        ModerationResponse moderationResponse = updatedActivity.getModerationResponse();
        if (moderationResponse != null) {
          System.out.println("Batch update moderation status: " + moderationResponse.getStatus());
          System.out.println("Batch update moderation action: " + moderationResponse.getRecommendedAction());
        }
      } catch (Exception e) {
        // If the batch update fails, log the error
        System.out.println("Batch update failed: " + e.getMessage());
      }
      
      // Wait a moment for moderation to complete
      Thread.sleep(1000);
      
      // Verify the activity in the feed
      List<Activity> activities = feed.getActivities().join();
      Activity fetchedActivity = null;
      for (Activity act : activities) {
        if (foreignID.equals(act.getForeignID())) {
          fetchedActivity = act;
          break;
        }
      }
      
      assertNotNull("Could not find the updated activity in feed", fetchedActivity);
      assertEquals("pissoar", fetchedActivity.getExtra().get("text"));
      
      // Check the moderation template update in the fetched activity
      System.out.println("Fetched batch moderation template: " + fetchedActivity.getModerationTemplate());
      
      // Check the moderation response in the fetched activity
      ModerationResponse fetchedModeration = fetchedActivity.getModerationResponse();
      if (fetchedModeration != null) {
        System.out.println("Fetched batch moderation status: " + fetchedModeration.getStatus());
        System.out.println("Fetched batch moderation action: " + fetchedModeration.getRecommendedAction());
      }
    } finally {
      // Clean up
      feed.removeActivityByForeignID(foreignID).join();
    }
  }
} 