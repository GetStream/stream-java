package io.getstream.client.apache.repo.utils;

import io.getstream.client.model.feeds.BaseFeed;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class StreamRepoUtilsTest {

    private static final String API_SECRET = "4vknf33hn4n94exgrg367jbmg4jxetem93bqcg3nkdf2xau3q8pmy3pftytq4w8v";

    private BaseFeed feed;

    @Before
    public void init() {
        this.feed = mock(BaseFeed.class);
        when(this.feed.getFeedId()).thenReturn("id");
    }

    @Test
    public void shouldAddAuthentication() throws Exception {
        HttpRequestBase request = mock(HttpRequestBase.class);
        String signature = StreamRepoUtils.createFeedSignature(feed, API_SECRET);

        StreamRepoUtils.addAuthentication(feed, API_SECRET, request);
        verify(request).addHeader(eq("Authorization"), eq(signature));
    }

    @Test
    public void shouldCreateFeedToken() throws Exception {
        assertThat(StreamRepoUtils.createFeedToken(feed, API_SECRET), is("0DayILmfWZ4i8h6WHFUV0diDXOg"));
    }

    @Test
    public void shouldCreateFeedSignature() throws Exception {
        assertThat(StreamRepoUtils.createFeedSignature(feed, API_SECRET), is("id 0DayILmfWZ4i8h6WHFUV0diDXOg"));
    }

    @Test
    public void shouldAddJwtAuthenticationPost() throws Exception {
        HttpPost request = mock(HttpPost.class);

        StreamRepoUtils.addJwtAuthentication("token", request);
        verify(request).addHeader(eq("Authorization"), eq("token"));
        verify(request).addHeader(eq("stream-auth-type"), eq("jwt"));
    }

    @Test
    public void shouldAddJwtAuthenticationGet() throws Exception {
        HttpGet request = mock(HttpGet.class);

        StreamRepoUtils.addJwtAuthentication("token", request);
        verify(request).addHeader(eq("Authorization"), eq("token"));
        verify(request).addHeader(eq("stream-auth-type"), eq("jwt"));
    }
}