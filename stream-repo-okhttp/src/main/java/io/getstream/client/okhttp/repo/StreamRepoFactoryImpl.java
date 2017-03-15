package io.getstream.client.okhttp.repo;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.repo.StreamRepoFactory;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.InfoUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Create a new StreamRepository using the ApacheHttpClient.
 */
public class StreamRepoFactoryImpl implements StreamRepoFactory {

	private static final String USER_AGENT_PREFIX = "stream-java-okhttp-%s";

	private final String userAgent;

	public StreamRepoFactoryImpl() {
		String version = "undefined";
		Properties properties = InfoUtil.getProperties();
		if (null != properties) {
			version = properties.getProperty(InfoUtil.VERSION);
		}
		this.userAgent = String.format(USER_AGENT_PREFIX, version);
	}

	@Override
    public StreamRepository newInstance(ClientConfiguration clientConfiguration,
                                        AuthenticationHandlerConfiguration authenticationHandlerConfiguration) {
        return new StreamRepositoryImpl(clientConfiguration, initClient(clientConfiguration, authenticationHandlerConfiguration));
    }

    private OkHttpClient initClient(final ClientConfiguration config, AuthenticationHandlerConfiguration authConfig) {
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
		client.setReadTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
		client.setWriteTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
		client.setRetryOnConnectionFailure(true);
		client.interceptors().add(new UserAgentInterceptor());
		client.interceptors().add(new HttpSignatureinterceptor(authConfig));
		client.setConnectionPool(new ConnectionPool(config.getMaxConnections(), config.getKeepAlive()));
		return client;
    }

	/**
	 * Add custom user-agent to the request.
	 */
	class UserAgentInterceptor implements Interceptor {
		@Override
		public Response intercept(Chain chain) throws IOException {
			return chain.proceed(chain.request().newBuilder().header("User-Agent", userAgent).build());
		}
	}
}
