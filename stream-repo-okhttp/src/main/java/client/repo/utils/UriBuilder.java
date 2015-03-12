/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package client.repo.utils;

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
     * @return
     */
    public static UriBuilder fromEndpoint(final String endpoint) {
        UriBuilder uriBuilder = new UriBuilder(endpoint);
        return uriBuilder;
    }

    /**
     * Create {@link UriBuilder} starting from a given URI.
     *
     * @param endpoint Endpoint in {@link java.net.URI} format.
     * @return
     */
    public static UriBuilder fromEndpoint(final URI endpoint) {
        UriBuilder uriBuilder = new UriBuilder(endpoint);
        return uriBuilder;
    }

    /**
     * Add a path component to the URI.
     *
     * @param path Path component(s) as String.
     * @return
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
     * @return
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
     * @return
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
     * @return
     */
    public UriBuilder queryParam(String name, Long value) {
        this.uri.addParameter(name, value.toString());
        return this;
    }

    /**
     * Build the final URI.
     *
     * @return
     */
    public URI build() throws UriBuilderException {
        try {
            return uri.build();
        } catch (URISyntaxException e) {
            throw new UriBuilderException("Cannot build valid URI", e);
        }
    }
}
