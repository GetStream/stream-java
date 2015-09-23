package io.getstream.client.apache.model.activities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.model.activities.SimpleActivity;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.hasItems;

public class ActivitySignedRecipientDeserializerTest {

    private static final String ACTIVITY_NO_SIGNATURE =
            "{\"id\":null,\"actor\":\"actor\",\"verb\":\"verb\",\"object\":\"object\"," +
                    "\"target\":\"target\",\"time\":null,\"to\":[\"user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac\", " +
                    "\"user:2 6mQhuzQ79e0rZ17bSq1CCxXoRac\"]," +
                    "\"origin\":null,\"duration\":null,\"foreign_id\":null}";

    private static final String ACTIVITY_WITH_SIGNATURE =
            "{\"actor\":\"actor\",\"duration\":\"151ms\",\"foreign_id\":null,\"id\":\"None\"," +
                    "\"object\":\"object\",\"origin\":null,\"target\":\"target\",\"time\":\"2015-02-06T10:45:19.112\"," +
                    "\"to\":[[\"user:1\",\"6mQhuzQ79e0rZ17bSq1CCxXoRac\"], " +
                    "[\"user:2\",\"6mQhuzQ79e0rZ17bSq1CCxXoRac\"]],\"verb\":\"verb\"}";

    @Test
    public void deserializeWithoutSignature() throws IOException {
        SimpleActivity a = new ObjectMapper().readValue(ACTIVITY_NO_SIGNATURE, SimpleActivity.class);
        MatcherAssert.assertThat(a.getTo(), hasItems("user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac", "user:2 6mQhuzQ79e0rZ17bSq1CCxXoRac"));
    }

    @Test
    public void deserializeWithSignature() throws IOException {
        SimpleActivity a = new ObjectMapper().readValue(ACTIVITY_WITH_SIGNATURE, SimpleActivity.class);
        MatcherAssert.assertThat(a.getTo(), hasItems("user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac", "user:2 6mQhuzQ79e0rZ17bSq1CCxXoRac"));
    }
}
