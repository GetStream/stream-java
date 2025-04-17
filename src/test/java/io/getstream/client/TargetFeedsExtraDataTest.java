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
        
        // 1. Create a test activity
        String activityId = UUID.randomUUID().toString();
        Activity activity = Activity.builder()
                .actor("test-user")
                .verb("post")
                .object("test-object")
                .foreignID("test-foreignId-" + activityId)
                .time(new Date())
                .build();
        
        Activity postedActivity = client.flatFeed("user", "test-user").addActivity(activity).join();
        
        // 2. Create a comment reaction on the activity
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("text", "This is a test comment");
        
        Reaction comment = Reaction.builder()
                .kind("comment")
                .activityID(postedActivity.getID())
                .extraField("data", commentData)
                .build();
        
        Reaction postedComment = client.reactions().add("test-user", comment, new FeedID[0]).join();
        
        // 3. Create a like reaction on the comment with targetFeedsExtraData
        Map<String, Object> targetFeedsExtraData = new HashMap<>();
        targetFeedsExtraData.put("parent_reaction", "SR:" + postedComment.getId());
        
        FeedID[] targetFeeds = new FeedID[] {
            new FeedID("notification", "test-user")
        };
        
        Reaction like = client.reactions().addChild(
                "test-user",
                "like",
                postedComment.getId(),
                targetFeeds,
                targetFeedsExtraData
        ).join();
        
        // 4. Verify that the reaction was created successfully
        assertNotNull("Like reaction should not be null", like);
        assertEquals("Like reaction should have kind='like'", "like", like.getKind());
        assertEquals("Like reaction should have parent ID", postedComment.getId(), like.getParent());
        
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
        client.flatFeed("user", "test-user").removeActivityByID(postedActivity.getID()).join();
    }
} 