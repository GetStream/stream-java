package io.getstream.client;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.service.StreamRepositoryRestImpl;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamClient {

    public static final String BASE_ENDPOINT = "https://api.getstream.io/api/v1.0/";

    private final AuthenticationHandlerConfiguration authenticationHandlerConfiguration;

    private final StreamRepositoryRestImpl streamRepository;

    public StreamClient(final ClientConfiguration clientConfiguration, final String key, final String secretKey) {
        checkNotNull(clientConfiguration, "Client configuration cannot be null.");
        this.authenticationHandlerConfiguration = new AuthenticationHandlerConfiguration();
        this.authenticationHandlerConfiguration.setApiKey(checkNotNull(key, "API key cannot be null."));
        this.authenticationHandlerConfiguration.setSecretKey(checkNotNull(secretKey, "API secret key cannot be null."));
        clientConfiguration.setAuthenticationHandlerConfiguration(authenticationHandlerConfiguration);
		this.streamRepository = new StreamRepositoryRestImpl(clientConfiguration);
    }

    public FeedFactory getFeedFactory() {
        return new FeedFactory(this);
    }

	public StreamRepositoryRestImpl getStreamRepository() {
		return streamRepository;
	}
}