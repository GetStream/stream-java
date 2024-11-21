package io.getstream.client;

import io.getstream.core.LookupKind;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Paginated;
import io.getstream.core.models.Reaction;
import java.util.List;

import io.getstream.core.options.Filter;
import io.getstream.core.options.Limit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReactionsClientTest {
  private static final String apiKey =
      System.getenv("STREAM_KEY") != null
          ? System.getenv("STREAM_KEY")
          : System.getProperty("STREAM_KEY");
  private static final String secret =
      System.getenv("STREAM_SECRET") != null
          ? System.getenv("STREAM_SECRET")
          : System.getProperty("STREAM_SECRET");

  @Test
  public void get() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder().activityID("ed2837a6-0a3b-4679-adc1-778a1704852d").kind("like").build();
    Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
    Reaction result = client.reactions().get(reply.getId()).join();
  }

  @Test
  public void filter() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Activity activity =
        client
            .flatFeed("flat", "reactor")
            .addActivity(Activity.builder().actor("this").verb("done").object("that").build())
            .join();

    client.reactions().add("john-doe", "like", activity.getID()).join();

    client.reactions().filter(LookupKind.ACTIVITY, activity.getID()).join();

    List<Reaction> result =
        client
            .reactions()
            .filter(LookupKind.ACTIVITY_WITH_DATA, activity.getID(), "comment")
            .join();
  }

  @Test
  public void filterWithUserID() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Activity activity =
        client
            .flatFeed("flat", "reactor")
            .addActivity(Activity.builder().actor("this").verb("done").object("that").build())
            .join();

    client.reactions().add("user1", "like", activity.getID()).join();
    client.reactions().add("user1", "comment", activity.getID()).join();
    client.reactions().add("user1", "share", activity.getID()).join();
    client.reactions().add("user2", "like", activity.getID()).join();
    client.reactions().add("user2", "comment", activity.getID()).join();
    client.reactions().add("user3", "comment", activity.getID()).join();

    List<Reaction> result = client.reactions().filter(LookupKind.ACTIVITY, activity.getID(), new Filter(), new Limit(10), "",false,  "user1").join();
    assertEquals(3, result.size());

    result = client.reactions().filter(LookupKind.ACTIVITY, activity.getID(), new Filter(), new Limit(10), "like",false, "user1").join();
    assertEquals(1, result.size());


    result = client.reactions().filter(LookupKind.ACTIVITY, activity.getID(), new Filter(), new Limit(10), "",false, "user2").join();
    assertEquals(2, result.size());

    result = client.reactions().filter(LookupKind.ACTIVITY, activity.getID(), new Filter(), new Limit(10), "",false, "user3").join();
    assertEquals(1, result.size());
  }

  @Test
  public void batchFetchReactions() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Activity activity =
        client
            .flatFeed("flat", "reactor")
            .addActivity(Activity.builder().actor("this").verb("done").object("that").build())
            .join();

    Reaction r1=client.reactions().add("user1", "like", activity.getID()).join();
    Reaction r2=client.reactions().add("user1", "comment", activity.getID()).join();
    Reaction r3=client.reactions().add("user1", "share", activity.getID()).join();
    Reaction r4=client.reactions().add("user2", "like", activity.getID()).join();
    Reaction r5=client.reactions().add("user2", "comment", activity.getID()).join();
    Reaction r6=client.reactions().add("user3", "comment", activity.getID()).join();

    List<Reaction> result = client.reactions().getBatch(List.of(r1.getId(), r2.getId(), r3.getId(), r4.getId(), r5.getId(), r6.getId())).join();
    assertEquals(6, result.size());

    assertEquals("like", result.get(0).getKind());
    assertEquals("comment", result.get(1).getKind());
    assertEquals("share", result.get(2).getKind());
    assertEquals("like", result.get(3).getKind());
    assertEquals("comment", result.get(4).getKind());
    assertEquals("comment", result.get(5).getKind());
  }

  @Test
  public void pagedFilter() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Activity activity =
        client
            .flatFeed("flat", "reactor")
            .addActivity(Activity.builder().actor("this").verb("done").object("that").build())
            .join();

    client.reactions().add("john-doe", "like", activity.getID()).join();

    client.reactions().paginatedFilter(LookupKind.ACTIVITY, activity.getID()).join();

    Paginated<Reaction> result =
        client
            .reactions()
            .paginatedFilter(LookupKind.ACTIVITY_WITH_DATA, activity.getID(), "comment")
            .join();
    while (result.getNext() != null && !result.getNext().isEmpty()) {
      result = client.reactions().paginatedFilter(result.getNext()).join();
    }
  }

  @Test
  public void add() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder().activityID("ed2837a6-0a3b-4679-adc1-778a1704852d").kind("like").build();
    client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
  }

  @Test
  public void addChild() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder().activityID("ed2837a6-0a3b-4679-adc1-778a1704852d").kind("like").build();
    data = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
    Reaction child = client.reactions().addChild("user-id", "like", data.getId()).join();
  }

  @Test
  public void update() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder()
            .id("b5c46f9b-0839-4207-86aa-6a7f388b7748")
            .kind("like")
            .extraField("key", "value")
            .build();
    client.reactions().update(data, new FeedID("flat", "1")).join();
  }

  @Test
  public void delete() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder().activityID("ed2837a6-0a3b-4679-adc1-778a1704852d").kind("like").build();
    Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
    client.reactions().delete(reply.getId()).join();
  }

  @Test
  public void softDelete() throws Exception {
    Client client = Client.builder(apiKey, secret).build();

    Reaction data =
        Reaction.builder().activityID("ed2837a6-0a3b-4679-adc1-778a1704852d").kind("like").build();
    Reaction reply = client.reactions().add("user-id", data, new FeedID("flat", "1")).join();
    client.reactions().softDelete(reply.getId()).join();
    client.reactions().restore(reply.getId()).join();
  }
}
