package io.getstream.client.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClientConfiguration {

    private int timeout = 5000;
    private int connectionTimeout = 500;
    private long timeToLive = 3600;
    private int maxConnections = 20;
	private StreamRegion region = StreamRegion.US_EAST;

    private AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private static ClientConfiguration fromJsonString(final String jsonString) throws IOException {
        checkNotNull(jsonString, "Input string cannot be null");
		return new ObjectMapper().readValue(jsonString, new TypeReference<ClientConfiguration>() {});
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

    public AuthenticationHandlerConfiguration getAuthenticationHandlerConfiguration() {
        return authenticationHandlerConfiguration;
    }

    public void setAuthenticationHandlerConfiguration(AuthenticationHandlerConfiguration authenticationHandlerConfiguration) {
        this.authenticationHandlerConfiguration = authenticationHandlerConfiguration;
    }
}
