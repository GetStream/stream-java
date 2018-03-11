package io.getstream.client.okhttp.repo.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import io.getstream.client.model.feeds.BaseFeed;
import okhttp3.Request;

public class StreamRepoUtilsTest {

    private static final String API_SECRET = "4vknf33hn4n94exgrg367jbmg4jxetem93bqcg3nkdf2xau3q8pmy3pftytq4w8v";

    private BaseFeed feed;

    @Before
    public void init() {
        this.feed = mock(BaseFeed.class);
        when(this.feed.getFeedId()).thenReturn("id");
    }

    @Test
    public void shouldAddAuthentication() {
        Request.Builder request = mock(Request.Builder.class);
        String signature = StreamRepoUtils.createFeedSignature(feed, API_SECRET);

        StreamRepoUtils.addAuthentication(feed, API_SECRET, request);
        verify(request).addHeader(eq("Authorization"), eq(signature));
    }

    @Test
    public void shouldCreateFeedToken() {
        assertThat(StreamRepoUtils.createFeedToken(feed, API_SECRET), is("0DayILmfWZ4i8h6WHFUV0diDXOg"));
    }

    @Test
    public void shouldCreateFeedSignature(){
        assertThat(StreamRepoUtils.createFeedSignature(feed, API_SECRET), is("id 0DayILmfWZ4i8h6WHFUV0diDXOg"));
    }

    @Test
    public void shouldAddJwtAuthentication() {
        Request.Builder request = mock(Request.Builder.class);

        StreamRepoUtils.addJwtAuthentication("token", request);
        verify(request).addHeader(eq("Authorization"), eq("token"));
        verify(request).addHeader(eq("stream-auth-type"), eq("jwt"));
    }
}