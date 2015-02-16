package io.getstream.client.config;

import io.getstream.client.utils.UriBuilder;

import java.net.URI;

/**
 * GetStream.io supports multiple locations. This is the list of available regions.
 */
public enum StreamRegion {

	US_EAST ("https://us-east-api.getstream.io/api"),
	US_WEST ("https://us-west-api.getstream.io/api");

	private final String endpoint;

	StreamRegion(String endpoint) {
		this.endpoint = endpoint;
	}

	public URI getEndpoint() {
		return UriBuilder.fromEndpoint(this.endpoint).path(VERSION).build();
	}

	private final static String VERSION = "v1.0";
}
