package io.getstream.client.repo;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;

/**
 * Create a new StreamRepository instance.
 */
public interface StreamRepoFactory {

    /**
     * Create a new StreamRepository instance.
     *
     * @param clientConfiguration Client configuration holder
     * @param authenticationHandlerConfiguration
     *                            Authentication handler (contains API key and secret key).
     * @return A new instance of a {@link StreamRepository}
     */
    StreamRepository newHttpClient(final ClientConfiguration clientConfiguration,
                                 final AuthenticationHandlerConfiguration authenticationHandlerConfiguration);

}
