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
