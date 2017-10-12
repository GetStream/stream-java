package io.getstream.client.config;

import io.getstream.client.exception.UriBuilderException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * GetStream.io supports multiple locations. This is the list of available regions.
 */
public enum StreamRegion {

    BASE("https://api.stream-io-api.com/api"),
    US_EAST("https://us-east-api.stream-io-api.com/api"),
    US_WEST("https://us-west-api.stream-io-api.com/api"),
    EU_WEST("https://eu-west-api.stream-io-api.com/api"),
    AP_NORTH_EAST("https://ap-northeast-api.stream-io-api.com/api"),
    AP_SOUTH_EAST("https://ap-southeast-api.stream-io-api.com/api"),
    LOCAL_TEST("http://localhost:8089/api"), /* used for testing purpose only */
    QA_TEST("https://qa-api.stream-io-api.com/api"); /* used for integration test */

    protected final static String VERSION = "v1.0";

    private final String endpoint;

    StreamRegion(String endpoint) {
        this.endpoint = endpoint;
    }

    public URI getEndpoint() {
        try {
            return new URI(this.endpoint.concat("/").concat(VERSION));
        } catch (URISyntaxException e) {
            throw new UriBuilderException();
        }
    }
}
