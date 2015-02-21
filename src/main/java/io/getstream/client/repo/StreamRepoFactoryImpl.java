package io.getstream.client.repo;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Create a new StreamRepository using the ApacheHttpClient.
 */
public class StreamRepoFactoryImpl implements StreamRepoFactory {
    @Override
    public StreamRepository newInstance(ClientConfiguration clientConfiguration,
                                        AuthenticationHandlerConfiguration authenticationHandlerConfiguration) {
        return new StreamRepositoryImpl(clientConfiguration, initClient(clientConfiguration));
    }

    private CloseableHttpClient initClient(final ClientConfiguration config) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(config.getTimeToLive(),
                       TimeUnit.MILLISECONDS);
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(config.getMaxConnections());
        return HttpClients.custom()
                       .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                       .setUserAgent("Apache-HttpClient/io.getstream java client")
                       .setDefaultRequestConfig(RequestConfig.custom()
                                                        .setConnectTimeout(config.getConnectionTimeout())
                                                        .setSocketTimeout(config.getTimeout()).build())
                       .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
                       .setMaxConnTotal(config.getMaxConnections())
                       .setConnectionManager(connectionManager).build();
    }
}
