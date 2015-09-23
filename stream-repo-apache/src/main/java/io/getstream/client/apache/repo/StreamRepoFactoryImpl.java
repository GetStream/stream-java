/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
package io.getstream.client.apache.repo;

import io.getstream.client.config.AuthenticationHandlerConfiguration;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.repo.StreamRepoFactory;
import io.getstream.client.repo.StreamRepository;
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
