package io.getstream.core.utils;

import static io.getstream.core.utils.Serialization.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.getstream.client.entities.Match;
import io.getstream.client.entities.VolleyballMatch;
import io.getstream.core.http.Response;
import io.getstream.core.models.Activity;
import io.getstream.core.models.CollectionData;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Reaction;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.junit.Ignore;
import org.junit.Test;

class SomeCustomType {
  public String id;
  public String key;
  public String collection;
  public Map<String, Object> extra;
}

public class SerializationTest {
  @Test
  public void activitySerialization() throws Exception {
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    Activity activity =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .to(new FeedID("hey:now"))
            .time(isoFormat.parse("2001-09-11T00:01:02.000"))
            .build();

    String result = new String(toJSON(activity), Charset.forName("UTF-8"));
    assertEquals(
        result,
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"time\":\"2001-09-11T00:01:02.000\",\"to\":[\"hey:now\"]}");
  }

  @Test
  public void activityWithExtraSerialization() throws Exception {
    Activity activityWithExtra =
        Activity.builder()
            .actor("test")
            .verb("test")
            .object("test")
            .extra(
                new ImmutableMap.Builder<String, Object>()
                    .put("int", 1)
                    .put("string", "2")
                    .put(
                        "extra",
                        new ImmutableMap.Builder<String, String>().put("test", "test").build())
                    .build())
            .build();
    String result = new String(toJSON(activityWithExtra), Charset.forName("UTF-8"));
    assertEquals(
        result,
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"int\":1,\"string\":\"2\",\"extra\":{\"test\":\"test\"}}");
  }

  @Test
  public void arbitraryObjectSerialization() throws Exception {
    Object customActivity =
        new Object() {
          public String actor = "test";
          public String verb = "test";
          public String object = "test";
          public int value = 1;
          public Map<String, String> nested =
              new ImmutableMap.Builder<String, String>().put("test", "test").build();
        };
    String result = new String(toJSON(customActivity), Charset.forName("UTF-8"));
    assertEquals(
        result,
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"value\":1,\"nested\":{\"test\":\"test\"}}");
  }

  // XXX: disabled due to JVM version serialization result variance
  @Test
  @Ignore
  public void arbitraryObjectConversion() throws Exception {
    Activity customActivity =
        Activity.builder()
            .fromCustomActivity(
                new Object() {
                  public String actor = "test";
                  public String verb = "test";
                  public String object = "test";
                  public int value = 1;
                  public Map<String, String> nested =
                      new ImmutableMap.Builder<String, String>().put("test", "test").build();
                })
            .build();
    String result = new String(toJSON(customActivity), Charset.forName("UTF-8"));
    assertEquals(
        result,
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"nested\":{\"test\":\"test\"},\"value\":1}");
  }

  @Test
  public void hierarchyObjectConversion() throws Exception {
    VolleyballMatch volley = new VolleyballMatch();
    volley.actor = "Me";
    volley.object = "Message";
    volley.verb = "verb";
    volley.setNrOfBlocked(1);
    volley.setNrOfServed(1);
    Activity to = convert(volley, Activity.class);
    Match from = convert(to, Match.class);
    assertEquals(from, volley);
  }

  @Test
  public void anyGetterConversion() throws Exception {
    CollectionData data =
        new CollectionData(
            "some",
            "id",
            new ImmutableMap.Builder<String, Object>()
                .put("key", "value")
                .put("extra", ImmutableMap.of("test", "test"))
                .build());

    SomeCustomType result = convert(data, SomeCustomType.class);
  }

  @Test
  public void activityDeserialization() throws Exception {
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    String activity =
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"time\":\"2019-01-10T15:08:53.442419\",\"to\":[\"hey:now\"]}";

    Activity result =
        fromJSON(
            new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), Activity.class);
    assertEquals(result.getActor(), "test");
    assertEquals(result.getVerb(), "test");
    assertEquals(result.getObject(), "test");
    assertEquals(result.getTo(), Lists.newArrayList(new FeedID("hey:now")));
    assertEquals(result.getTime(), isoFormat.parse("2019-01-10T15:08:53.442"));
  }

  @Test
  public void enrichedActivityDeserialization() throws Exception {
    String activity =
        "{\"actor\":{\"id\":\"tester\",\"other\":\"field\"},\"verb\":\"tests\",\"object\":\"test\"}";

    EnrichedActivity result =
        fromJSON(
            new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))),
            EnrichedActivity.class);
    assertEquals(result.getActor().getID(), "tester");
    assertEquals(result.getVerb(), "tests");
    assertEquals(result.getObject().getID(), "test");
  }

  @Test
  public void activityWithExtraDeserialization() throws Exception {
    String activity =
        "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"value\":1,\"extra\":{\"test\":\"test\"}}";

    Activity result =
        fromJSON(
            new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), Activity.class);
    assertEquals(result.getActor(), "test");
    assertEquals(result.getVerb(), "test");
    assertEquals(result.getObject(), "test");
    assertEquals(
        result.getExtra(),
        new ImmutableMap.Builder<String, Object>()
            .put("value", 1)
            .put("extra", ImmutableMap.of("test", "test"))
            .build());
  }

  @Test
  public void customDeserialization() throws Exception {
    String activityGroup =
        "{\"activities\":[{\"actor\":\"Me\",\"foreign_id\":\"\",\"id\":\"a42ae9e1-d3b3-11e8-93d1-0a9265761cda\",\"nr_of_blocked\":1,\"nr_of_served\":1,\"object\":\"Message\",\"origin\":null,\"target\":\"\",\"time\":\"2018-10-19T15:28:34.168266\",\"type\":\"volley\",\"verb\":\"verb\"},{\"actor\":\"Me\",\"foreign_id\":\"\",\"id\":\"a42aea1d-d3b3-11e8-93d2-0a9265761cda\",\"nr_of_penalty\":2,\"nr_of_score\":3,\"object\":\"Message\",\"origin\":null,\"target\":\"\",\"time\":\"2018-10-19T15:28:34.168272\",\"type\":\"football\",\"verb\":\"verb\"}],\"duration\":\"8.05ms\"}";
    Response response =
        new Response(
            200, new ByteArrayInputStream(activityGroup.getBytes(Charset.forName("UTF-8"))));

    List<Match> result = deserializeContainer(response, "activities", Match.class);
  }

  @Test
  public void emptyMapDeserialization() throws Exception {
    String activity = "{}";
    TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};

    Map<String, Object> result =
        fromJSON(new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), type);
  }

  @Test
  public void emptyDataDeserialization() throws Exception {
    String reaction = "{ \"data\": {} }";
    TypeReference<Reaction> type = new TypeReference<Reaction>() {};

    Reaction result =
        fromJSON(new ByteArrayInputStream(reaction.getBytes(Charset.forName("UTF-8"))), type);
    assertNotNull(result);
  }

  @Test
  public void creatorAndAnySetterDeserialization() throws Exception {
    String data = "{\"id\":\"id-thing\",\"value\":1,\"extra\":{\"test\":\"test\"}}";

    CollectionData result =
        fromJSON(
            new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8"))),
            CollectionData.class);
    assertEquals(result.getID(), "id-thing");
    assertEquals(
        result.getData(),
        new ImmutableMap.Builder<String, Object>()
            .put("value", 1)
            .put("extra", ImmutableMap.of("test", "test"))
            .build());
  }
}
