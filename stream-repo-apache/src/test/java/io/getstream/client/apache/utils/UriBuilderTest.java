package io.getstream.client.apache.utils;

import io.getstream.client.apache.repo.utils.UriBuilder;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UriBuilderTest {

    @Test
    public void shouldConcatPath() {
        MatcherAssert.assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo").path("bar").build().toString(),
                                        is("http://example.com/foo/bar"));
    }

    @Test
    public void shouldConcatPathCleaningSlashes() {
        MatcherAssert.assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo/").path("bar").build().toString(),
                                        is("http://example.com/foo/bar"));
    }

    @Test
    public void shouldConcatPathPreservingFinalSlashes() {
        MatcherAssert.assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo/").path("bar/").build().toString(),
                                        is("http://example.com/foo/bar/"));
    }

    @Test
    public void shouldConcatPathAndQueryParams() {
        MatcherAssert.assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo").path("bar")
                                         .queryParam("foo", "bar").build().toString(), is("http://example.com/foo/bar?foo=bar"));
    }
}
