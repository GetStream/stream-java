package io.getstream.client.apache.repo;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.repo.StreamRepoFactory;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.util.InfoUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Create a new StreamRepository using the ApacheHttpClient.
 */
public class StreamRepoFactoryImpl implements StreamRepoFactory {

    private static final String USER_AGENT_PREFIX = "stream-java-apache-%s";

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

    private CloseableHttpClient initClient(final ClientConfiguration config,
                                           AuthenticationHandlerConfiguration authConfig) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(config.getTimeToLive(),
                TimeUnit.MILLISECONDS);
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(config.getMaxConnections());

        return HttpClients.custom()
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setUserAgent(userAgent)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(config.getConnectionTimeout())
                        .setSocketTimeout(config.getTimeout()).build())
                .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
                .setMaxConnTotal(config.getMaxConnections())
                .setConnectionManager(connectionManager)
                .addInterceptorLast(new HttpSignatureInterceptor(authConfig))
                .build();
    }
}
