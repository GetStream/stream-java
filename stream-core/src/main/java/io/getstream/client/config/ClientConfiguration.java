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
package io.getstream.client.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * ClientConfiguration holds configuration params for the http client.
 */
public class ClientConfiguration {

    /**
     * Socket timeout in ms.
     */
    private int timeout = 5000;

    /**
     * Connection timeout in ms.
     */
    private int connectionTimeout = 500;

    /**
     * TimeToLive in ms.
     */
    private long timeToLive = 3600;

	/**
	 * Keep alive in ms.
	 */
	private long keepAlive = 3000;

    /**
     * Max concurrent connection the pool should handle.
     */
    private int maxConnections = 20;

    /**
     * Max concurrent connection per route.
     */
    private int maxConnectionsPerRoute = 20;

    /**
     * Stream location.
     */
    private StreamRegion region = StreamRegion.US_EAST;

    private AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    /**
     * Default constructor.
     */
    public ClientConfiguration() {
        super();
    }

    /**
     * Create a configuration using a given region.
     * @param region Region
     */
    public ClientConfiguration(final StreamRegion region) {
        super();
        this.region = region;
    }

    /**
     * Create a ClientConfiguration bean from a given json string.
     *
     * @param jsonString Json representation of ClientConfiguration bean.
     * @return
     * @throws IOException in case the object cannot be parsed correctly.
     */
    private static ClientConfiguration fromJsonString(final String jsonString) throws IOException {
        checkNotNull(jsonString, "Input string cannot be null");
        return new ObjectMapper().readValue(jsonString, new TypeReference<ClientConfiguration>() {
        });
    }

    public StreamRegion getRegion() {
        return region;
    }

    public void setRegion(StreamRegion region) {
        this.region = region;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public AuthenticationHandlerConfiguration getAuthenticationHandlerConfiguration() {
        return authenticationHandlerConfiguration;
    }

    public void setAuthenticationHandlerConfiguration(AuthenticationHandlerConfiguration authenticationHandlerConfiguration) {
        this.authenticationHandlerConfiguration = authenticationHandlerConfiguration;
    }

	public long getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}
}
