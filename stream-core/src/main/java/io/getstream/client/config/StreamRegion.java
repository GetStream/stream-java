package io.getstream.client.config;

import io.getstream.client.exception.UriBuilderException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * GetStream.io supports multiple locations. This is the list of available regions.
 */
public enum StreamRegion {

    US_EAST("https://us-east-api.getstream.io/api"),
    US_WEST("https://us-west-api.getstream.io/api"),
    EU_WEST("https://eu-west-api.getstream.io/api"),
    AP_NORTH_EAST("https://ap-northeast-api.getstream.io/api"),
    AP_SOUTH_EAST("https://ap-southeast-api.getstream.io/api"),
    LOCAL_TEST("http://localhost:8089/api"), /* used for testing purpose only */
    QA_TEST("http://qa-api.getstream.io/api"), /* used for integration test */
    SNI_TEST("https://sni-api.getstream.io/api"); /* used for testing purpose only */

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
