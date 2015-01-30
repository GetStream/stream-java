package io.getstream.client.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ClientConfiguration {

    private long timeout = 500;
    private long connectionTimeout = 500;
    private long timeToLive = 3600;
    private boolean cookiesEnabled = false;
    private int maxConnections = 1024;
    private int maxConnectionsPerRoute = 1024;
    private long keepAlive = 0;
    private int retries = 0;
	private StreamRegion region = StreamRegion.US_EAST;

    private AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private static ClientConfiguration fromJsonString(final String jsonString) throws IOException {
		return new ObjectMapper().readValue(jsonString, new TypeReference<ClientConfiguration>() {});
    }

	public StreamRegion getRegion() {
		return region;
	}

	public void setRegion(StreamRegion region) {
		this.region = region;
	}

	public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(boolean cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
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

    public long getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public AuthenticationHandlerConfiguration getAuthenticationHandlerConfiguration() {
        return authenticationHandlerConfiguration;
    }

    public void setAuthenticationHandlerConfiguration(AuthenticationHandlerConfiguration authenticationHandlerConfiguration) {
        this.authenticationHandlerConfiguration = authenticationHandlerConfiguration;
    }
}
