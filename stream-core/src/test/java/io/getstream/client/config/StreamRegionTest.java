package io.getstream.client.config;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StreamRegionTest {

    @Test
    public void shouldGetEndpoint() {
        assertThat(StreamRegion.US_EAST.getEndpoint().toString(), is("https://us-east-api.getstream.io/api/" + StreamRegion.VERSION));
    }
}