package io.getstream.client;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.Feed;
import io.getstream.client.model.FlatFeed;
import io.getstream.client.model.SimpleActivity;
import io.getstream.client.model.UserFeed;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class IntegrationTest {

    @Test
    public void shouldGetFeed() throws IOException, SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "key", "secretKey");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "1");
        SimpleActivity simpleActivity = new SimpleActivity();
        simpleActivity.setActor("1");
        simpleActivity.setObject("2");
        simpleActivity.setVerb("3");
        flatFeed.addActivity(simpleActivity);
    }

}
