package io.getstream.client.util;

import io.getstream.client.config.ClientConfiguration;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EndpointUtilTest {

    @Test
    public void shouldGetPersonalizedEndpoint() throws Exception {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setPersonalizedFeedEndpoint("http://yourcompany.getstream.io/yourcompany");

        URI personalizedEndpoint = EndpointUtil.getPersonalizedEndpoint(clientConfiguration);
        assertThat(personalizedEndpoint.toString(), is("http://yourcompany.getstream.io/yourcompany/"));
    }

    @Test
    public void shouldGetPersonalizedEndpointWithoutDoubleSlash() throws Exception {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setPersonalizedFeedEndpoint("http://yourcompany.getstream.io/yourcompany/");

        URI personalizedEndpoint = EndpointUtil.getPersonalizedEndpoint(clientConfiguration);
        assertThat(personalizedEndpoint.toString(), is("http://yourcompany.getstream.io/yourcompany/"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFail() throws Exception {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setPersonalizedFeedEndpoint(null);

        EndpointUtil.getPersonalizedEndpoint(clientConfiguration);
    }
}