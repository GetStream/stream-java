package io.getstream.client;

import io.getstream.core.LookupKind;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Reaction;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TargetFeedsExtraDataTest {
    private static final String apiKey =
        System.getenv("STREAM_KEY") != null
            ? System.getenv("STREAM_KEY")
            : System.getProperty("STREAM_KEY");
    private static final String secret =
        System.getenv("STREAM_SECRET") != null
            ? System.getenv("STREAM_SECRET")
            : System.getProperty("STREAM_SECRET");
    
    @Test
    public void testTargetFeedsExtraData() throws Exception {
        // Create client
        Client client = Client.builder(apiKey, secret).build();
        
        // Use unique user id to avoid conflicts in notification feed
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        String userId = "test-user-" + uniqueId;
        
        // 1. Create a test activity
        String activityId = UUID.randomUUID().toString();
        Activity activity = Activity.builder()
                .actor(userId)
                .verb("post")
                .object("test-object")
                .foreignID("test-foreignId-" + activityId)
                .time(new Date())
                .build();
        
        Activity postedActivity = client.flatFeed("user", userId).addActivity(activity).join();
        
        // 2. Create a comment reaction on the activity
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("text", "This is a test comment");
        
        Reaction comment = Reaction.builder()
                .kind("comment")
                .activityID(postedActivity.getID())
                .extraField("data", commentData)
                .build();
        
        Reaction postedComment = client.reactions().add(userId, comment, new FeedID[0]).join();
        
        // 3. Create a like reaction on the comment with targetFeedsExtraData
        Map<String, Object> targetFeedsExtraData = new HashMap<>();
        String extraDataValue = "SR:" + postedComment.getId();
        targetFeedsExtraData.put("parent_reaction", extraDataValue);
        
        FeedID[] targetFeeds = new FeedID[] {
            new FeedID("notification", userId)
        };
        
        // The critical part of the test: Can we successfully create a reaction with targetFeedsExtraData
        Reaction like = client.reactions().addChild(
                "actor-" + uniqueId, // Different user performs the like action
                "like",
                postedComment.getId(),
                targetFeeds,
                targetFeedsExtraData
        ).join();
        
        // 4. Verify that the reaction was created successfully
        assertNotNull("Like reaction should not be null", like);
        assertEquals("Like reaction should have kind='like'", "like", like.getKind());
        assertEquals("Like reaction should have parent ID", postedComment.getId(), like.getParent());
        
        // Check if the reaction has extra data
        Map<String, Object> reactionExtra = like.getExtra();
        assertNotNull("Reaction should have extra data", reactionExtra);
        
        // Check for targetFeedsExtraData directly
        assertTrue("Reaction should contain target_feeds_extra_data", 
                reactionExtra.containsKey("target_feeds_extra_data"));
        
        // Verify the content of targetFeedsExtraData
        Object extraDataObj = reactionExtra.get("target_feeds_extra_data");
        assertTrue("target_feeds_extra_data should be a Map", extraDataObj instanceof Map);
        
        Map<String, Object> extraDataMap = (Map<String, Object>) extraDataObj;
        assertTrue("target_feeds_extra_data should contain parent_reaction", 
                extraDataMap.containsKey("parent_reaction"));
        assertEquals("parent_reaction value should match what we sent", 
                extraDataValue, extraDataMap.get("parent_reaction"));
        
        // 5. Get the reactions to verify
        List<Reaction> reactions = client.reactions().filter(
                LookupKind.REACTION,
                postedComment.getId(),
                "like"
        ).join();
        
        assertEquals("Should have one like reaction", 1, reactions.size());
        assertEquals("Reaction should match the one we created", like.getId(), reactions.get(0).getId());
        
        // Clean up
        client.reactions().delete(like.getId()).join();
        client.reactions().delete(postedComment.getId()).join();
        client.flatFeed("user", userId).removeActivityByID(postedActivity.getID()).join();
    }
} 