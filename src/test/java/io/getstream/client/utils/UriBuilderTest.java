package io.getstream.client.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UriBuilderTest {

	@Test
	public void shouldConcatPath() {
		assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo").path("bar").build().toString(),
				is("http://example.com/foo/bar"));
	}

	@Test
	public void shouldConcatPathCleaningSlashes() {
		assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo/").path("bar").build().toString(),
				is("http://example.com/foo/bar"));
	}

	@Test
	public void shouldConcatPathPreservingFinalSlashes() {
		assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo/").path("bar/").build().toString(),
				is("http://example.com/foo/bar/"));
	}

	@Test
	public void shouldConcatPathAndQueryParams() {
		assertThat(UriBuilder.fromEndpoint("http://example.com/").path("foo").path("bar")
				.queryParam("foo", "bar").build().toString(), is("http://example.com/foo/bar?foo=bar"));
	}
}
