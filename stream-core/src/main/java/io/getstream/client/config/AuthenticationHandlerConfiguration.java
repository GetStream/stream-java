package io.getstream.client.config;

/**
 * Bean designed to hold authentication credentials to access
 * getstream.io.
 */
public class AuthenticationHandlerConfiguration {

    /**
     * Getstream.io's API key.
     */
    private String apiKey;

    /**
     * Getstream.io's secret key.
     */
    private String secretKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
