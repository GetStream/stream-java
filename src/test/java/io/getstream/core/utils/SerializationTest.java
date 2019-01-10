package io.getstream.core.utils;

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
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static io.getstream.core.utils.Serialization.*;
import static org.junit.jupiter.api.Assertions.*;

class SomeCustomType {
    public String id;
    public String key;
    public String collection;
    public Map<String, Object> extra;
}

class SerializationTest {
    @Test
    void activitySerialization() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String[] result = new String[1];
        Activity activity = Activity.builder()
                .actor("test")
                .verb("test")
                .object("test")
                .to(new FeedID("hey:now"))
                .time(isoFormat.parse("2001-09-11T00:01:02.000000"))
                .build();

        assertDoesNotThrow(() -> {
            result[0] = new String(toJSON(activity), Charset.forName("UTF-8"));
        });
        assertEquals(result[0], "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"time\":\"2001-09-11T00:01:02.0\",\"to\":[\"hey:now\"]}");
    }

    @Test
    void activityWithExtraSerialization() {
        String[] result = new String[1];
        Activity activityWithExtra = Activity.builder()
                .actor("test")
                .verb("test")
                .object("test")
                .extra(new ImmutableMap.Builder<String, Object>()
                        .put("int", 1)
                        .put("string", "2")
                        .put("extra", new ImmutableMap.Builder<String, String>()
                                .put("test", "test")
                                .build())
                        .build())
                .build();
        assertDoesNotThrow(() -> {
            result[0] = new String(toJSON(activityWithExtra), Charset.forName("UTF-8"));
        });
        assertEquals(result[0], "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"int\":1,\"string\":\"2\",\"extra\":{\"test\":\"test\"}}");
    }

    @Test
    void arbitraryObjectSerialization() {
        String[] result = new String[1];
        Object customActivity = new Object() {
            public String actor = "test";
            public String verb = "test";
            public String object = "test";
            public int value = 1;
            public Map<String, String> nested = new ImmutableMap.Builder<String, String>()
                    .put("test", "test")
                    .build();
        };
        assertDoesNotThrow(() -> {
            result[0] = new String(toJSON(customActivity), Charset.forName("UTF-8"));
        });
        assertEquals(result[0], "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"value\":1,\"nested\":{\"test\":\"test\"}}");
    }

    @Test
    void arbitraryObjectConversion() {
        String[] result = new String[1];
        Activity customActivity = Activity.builder().fromCustomActivity(new Object() {
            public String actor = "test";
            public String verb = "test";
            public String object = "test";
            public int value = 1;
            public Map<String, String> nested = new ImmutableMap.Builder<String, String>()
                    .put("test", "test")
                    .build();
        }).build();
        assertDoesNotThrow(() -> {
            result[0] = new String(toJSON(customActivity), Charset.forName("UTF-8"));
        });
        assertEquals(result[0], "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"value\":1,\"nested\":{\"test\":\"test\"}}");
    }

    @Test
    void hierarchyObjectConversion() {
        Activity[] to = new Activity[1];
        Match[] from = new Match[1];

        VolleyballMatch volley = new VolleyballMatch();
        volley.actor = "Me";
        volley.object = "Message";
        volley.verb = "verb";
        volley.setNrOfBlocked(1);
        volley.setNrOfServed(1);
        assertDoesNotThrow(() -> {
            to[0] = convert(volley, Activity.class);
        });
        assertDoesNotThrow(() -> {
            from[0] = convert(to[0], Match.class);
        });
        assertEquals(from[0], volley);
    }

    @Test
    void anyGetterConversion() {
        SomeCustomType[] result = new SomeCustomType[1];
        CollectionData data = new CollectionData("some", "id", new ImmutableMap.Builder<String, Object>()
                .put("key", "value")
                .put("extra", ImmutableMap.of("test", "test"))
                .build());

        assertDoesNotThrow(() -> {
            result[0] = convert(data, SomeCustomType.class);
        });
    }

    @Test
    void activityDeserialization() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Activity[] result = new Activity[1];
        String activity = "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"time\":\"2001-09-11T00:01:02.000000\",\"to\":[\"hey:now\"]}";

        assertDoesNotThrow(() -> {
            result[0] = fromJSON(new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), Activity.class);
        });
        assertEquals(result[0].getActor(), "test");
        assertEquals(result[0].getVerb(), "test");
        assertEquals(result[0].getObject(), "test");
        assertEquals(result[0].getTo(), Lists.newArrayList(new FeedID("hey:now")));
        assertEquals(result[0].getTime(), isoFormat.parse("2001-09-11T00:01:02.000000"));
    }

    @Test
    void enrichedActivityDeserialization() throws ParseException {
        EnrichedActivity[] result = new EnrichedActivity[1];
        String activity = "{\"actor\":{\"id\":\"tester\",\"other\":\"field\"},\"verb\":\"tests\",\"object\":\"test\"}";

        assertDoesNotThrow(() -> {
            result[0] = fromJSON(new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), EnrichedActivity.class);
        });
        assertEquals(result[0].getActor().getID(), "tester");
        assertEquals(result[0].getVerb(), "tests");
        assertEquals(result[0].getObject().getID(), "test");
    }

    @Test
    void activityWithExtraDeserialization() {
        Activity[] result = new Activity[1];
        String activity = "{\"actor\":\"test\",\"verb\":\"test\",\"object\":\"test\",\"value\":1,\"extra\":{\"test\":\"test\"}}";

        assertDoesNotThrow(() -> {
            result[0] = fromJSON(new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), Activity.class);
        });
        assertEquals(result[0].getActor(), "test");
        assertEquals(result[0].getVerb(), "test");
        assertEquals(result[0].getObject(), "test");
        assertEquals(result[0].getExtra(), new ImmutableMap.Builder<String, Object>()
                .put("value", 1)
                .put("extra", ImmutableMap.of("test", "test"))
                .build());
    }

    @Test
    void customDeserialization() {
        List<Match>[] result = new List[1];
        String activityGroup = "{\"activities\":[{\"actor\":\"Me\",\"foreign_id\":\"\",\"id\":\"a42ae9e1-d3b3-11e8-93d1-0a9265761cda\",\"nr_of_blocked\":1,\"nr_of_served\":1,\"object\":\"Message\",\"origin\":null,\"target\":\"\",\"time\":\"2018-10-19T15:28:34.168266\",\"type\":\"volley\",\"verb\":\"verb\"},{\"actor\":\"Me\",\"foreign_id\":\"\",\"id\":\"a42aea1d-d3b3-11e8-93d2-0a9265761cda\",\"nr_of_penalty\":2,\"nr_of_score\":3,\"object\":\"Message\",\"origin\":null,\"target\":\"\",\"time\":\"2018-10-19T15:28:34.168272\",\"type\":\"football\",\"verb\":\"verb\"}],\"duration\":\"8.05ms\"}";
        Response response = new Response(200, new ByteArrayInputStream(activityGroup.getBytes(Charset.forName("UTF-8"))));

        assertDoesNotThrow(() -> {
            result[0] = deserializeContainer(response, "activities", Match.class);
        });
    }

    @Test
    void emptyMapDeserialization() {
        Map<String, Object>[] result = new Map[1];
        String activity = "{}";
        TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};

        assertDoesNotThrow(() -> {
            result[0] = fromJSON(new ByteArrayInputStream(activity.getBytes(Charset.forName("UTF-8"))), type);
        });
        assertNotNull(result[0]);
    }

    @Test
    void creatorAndAnySetterDeserialization() {
        CollectionData[] result = new CollectionData[1];
        String data = "{\"id\":\"id-thing\",\"value\":1,\"extra\":{\"test\":\"test\"}}";

        assertDoesNotThrow(() -> {
            result[0] = fromJSON(new ByteArrayInputStream(data.getBytes(Charset.forName("UTF-8"))), CollectionData.class);
        });
        assertEquals(result[0].getID(), "id-thing");
        assertEquals(result[0].getData(), new ImmutableMap.Builder<String, Object>()
                .put("value", 1)
                .put("extra", ImmutableMap.of("test", "test"))
                .build());
    }
}
