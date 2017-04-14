package io.getstream.client.apache.repo.utils;

import io.getstream.client.exception.UriBuilderException;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * {@link URIBuilder} wrapper class. It helps to compose paths with
 * and/or without attributes using a fluid pattern.
 * This class has a limited implementation and it has been built to meet
 * the current needs.
 */
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

    /**
     * Create {@link UriBuilder} starting from a given string.
     *
     * @param endpoint Endpoint in string format.
     * @return A new UriBuilder
     */
    public static UriBuilder fromEndpoint(final String endpoint) {
        UriBuilder uriBuilder = new UriBuilder(endpoint);
        return uriBuilder;
    }

    /**
     * Create {@link UriBuilder} starting from a given URI.
     *
     * @param endpoint Endpoint in {@link URI} format.
     * @return A new UriBuilder
     */
    public static UriBuilder fromEndpoint(final URI endpoint) {
        UriBuilder uriBuilder = new UriBuilder(endpoint);
        return uriBuilder;
    }

    /**
     * Add a path component to the URI.
     *
     * @param path Path component(s) as String.
     * @return itself
     */
    public UriBuilder path(String path) {
        String uriPath = uri.getPath();
        if (null != uriPath && uriPath.endsWith(PATH_SEPARATOR)) {
            this.uri.setPath(uriPath.concat(path));
        } else {
            this.uri.setPath(uriPath.concat(PATH_SEPARATOR).concat(path));
        }
        return this;
    }

    /**
     * Add a query param (e.g: ?param=key)
     *
     * @param name  Name of the param.
     * @param value Value of the param.
     * @return itself
     */
    public UriBuilder queryParam(String name, String value) {
        this.uri.addParameter(name, value);
        return this;
    }

    /**
     * Add a query param (e.g: ?param=key)
     *
     * @param name  Name of the param.
     * @param value Value of the numeric param (must be a {@link Integer}.
     * @return itself
     */
    public UriBuilder queryParam(String name, Integer value) {
        this.uri.addParameter(name, value.toString());
        return this;
    }

    /**
     * Add a query param (e.g: ?param=key)
     *
     * @param name  Name of the param.
     * @param value Value of the numeric param (must be a {@link Long}.
     * @return itself
     */
    public UriBuilder queryParam(String name, Long value) {
        this.uri.addParameter(name, value.toString());
        return this;
    }

    /**
     * Build the final URI.
     *
     * @return A {@link URI}
     */
    public URI build() throws UriBuilderException {
        try {
            return uri.build();
        } catch (URISyntaxException e) {
            throw new UriBuilderException("Cannot build valid URI", e);
        }
    }
}
