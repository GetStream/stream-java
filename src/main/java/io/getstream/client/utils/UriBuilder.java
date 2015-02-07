package io.getstream.client.utils;

import io.getstream.client.exception.UriBuilderException;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class UriBuilder {

	private static final String PATH_SEPARATOR = "/";

	private final URIBuilder uri;

	private UriBuilder(URI baseEndpoint) {
		this.uri = new URIBuilder(baseEndpoint);
	}

	private UriBuilder(String baseEndpoint) {
		try {
			this.uri = new URIBuilder(baseEndpoint);
		} catch (URISyntaxException e) {
			throw new UriBuilderException("Cannot build valid URI", e);
		}
	}

	public static UriBuilder fromEndpoint(final String endpoint) {
		UriBuilder uriBuilder = new UriBuilder(endpoint);
		return uriBuilder;
	}

	public static UriBuilder fromEndpoint(final URI endpoint) {
		UriBuilder uriBuilder = new UriBuilder(endpoint);
		return uriBuilder;
	}

	public UriBuilder path(String path) {
		String uriPath = uri.getPath();
		if (null != uriPath && uriPath.endsWith(PATH_SEPARATOR)) {
			this.uri.setPath(uriPath.concat(path));
		} else {
			this.uri.setPath(uriPath.concat(PATH_SEPARATOR).concat(path));
		}
		return this;
	}

	public UriBuilder queryParam(String name, String value) {
		this.uri.addParameter(name, value);
		return this;
	}

	public UriBuilder queryParam(String name, Integer value) {
		this.uri.addParameter(name, value.toString());
		return this;
	}

	public UriBuilder queryParam(String name, Long value) {
		this.uri.addParameter(name, value.toString());
		return this;
	}

	public URI build() throws UriBuilderException {
		try {
			return uri.build();
		} catch (URISyntaxException e) {
			throw new UriBuilderException("Cannot build valid URI", e);
		}
	}
}
