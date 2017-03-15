package io.getstream.client.okhttp.repo.utils;

import io.getstream.client.exception.UriBuilderException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * It helps to compose paths with
 * and/or without attributes using a fluid pattern.
 * This class has a limited implementation of UriBuilder and it has been built to meet
 * the current needs.
 */
public class UriBuilder {

    private static final String PATH_SEPARATOR = "/";

	private URI uri;

	private StringBuilder uriPath = new StringBuilder();

	private StringBuilder uriParams = new StringBuilder();

    private UriBuilder(URI baseEndpoint) {
        this.uri = baseEndpoint;
		if (uri.getPath() != null) {
			uriPath.append(uri.getPath());
		}
		if (uri.getQuery() != null) {
			uriParams.append(uri.getQuery());
		}
    }

	private UriBuilder(String baseEndpoint) {
		this.uri = URI.create(baseEndpoint);
		if (uri.getPath() != null) {
			uriPath.append(uri.getPath());
		}
		if (uri.getQuery() != null) {
			uriParams.append(uri.getQuery());
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
     * @param endpoint Endpoint in {@link java.net.URI} format.
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
        if (uriPath.toString().endsWith(PATH_SEPARATOR)) {
			uriPath.append(path);
        } else {
			uriPath.append(PATH_SEPARATOR).append(path);
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
		if (uriParams.length() > 0) {
			uriParams.append('&');
		}
        uriParams.append(name).append('=').append(value);
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
		if (uriParams.length() > 0) {
			uriParams.append('&');
		}
		uriParams.append(name).append('=').append(String.valueOf(value));
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
		if (uriParams.length() > 0) {
			uriParams.append('&');
		}
		uriParams.append(name).append('=').append(String.valueOf(value));
		return this;
    }

    /**
     * Build the final URI.
     *
     * @return A {@link URI}
     */
    public URI build() throws UriBuilderException {
		String finalUriPath = null;
		if (uriPath.length() > 0) {
			finalUriPath = uriPath.toString();
		}

		String finalUriParams = null;
		if (uriParams.length() > 0) {
			finalUriParams = uriParams.toString();
		}

		try {
			return new URI(uri.getScheme(), null, uri.getHost(),
					uri.getPort(), finalUriPath, finalUriParams, null);
		} catch (URISyntaxException e) {
			throw new UriBuilderException("Cannot build valid URI", e);
		}
	}
}
