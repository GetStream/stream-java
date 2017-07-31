package io.getstream.client.util;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.UriBuilderException;

import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class to parse and build up the Personalized feed endpoint.
 */
public class EndpointUtil {

    private EndpointUtil() {
    }

    /**
     * Get the personalized endpoint.
     * @param streamClient StreamClient's configuration which contain the Personalized endpoint.
     * @return The personalized endpoint.
     */
    public static URI getPersonalizedEndpoint(ClientConfiguration streamClient) {
        final String endpoint = streamClient.getPersonalizedFeedEndpoint();
        checkNotNull(endpoint, "Personalized url cannot be null");
        try {
            if (endpoint.endsWith("/")) {
                return new URI(endpoint);
            } else {
                return new URI(endpoint.concat("/"));
            }
        } catch (URISyntaxException e) {
            throw new UriBuilderException("Malformed personalized feed's URL.");
        }
    }
}
