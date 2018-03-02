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
    private int connectionTimeout = 1500;

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

    /**
     * Personalized feed endpoint
     */
    private String personalizedFeedEndpoint;

    /**
     * Set a custom endpoint.
     * If set the client will use the given endpoint instead of the default one associated with
     * the selected region.
     * It is usually not needed to provide an endpoint here.
     */
    private String defaultEndpoint;


    private AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private String proxyHost;

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    private Integer proxyPort;

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

    public String getPersonalizedFeedEndpoint() {
        return personalizedFeedEndpoint;
    }

    public void setPersonalizedFeedEndpoint(String personalizedFeedEndpoint) {
        this.personalizedFeedEndpoint = personalizedFeedEndpoint;
    }

    public String getDefaultEndpoint() {
        return defaultEndpoint;
    }

    public void setDefaultEndpoint(String defaultEndpoint) {
        this.defaultEndpoint = defaultEndpoint;
    }
}
