package io.getstream.client;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.feeds.FeedFollow;
import io.getstream.client.model.feeds.FlatFeed;
import io.getstream.client.model.activities.SimpleActivity;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class IntegrationTest {

    @Test
    public void shouldGetFollowers() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
        List<FeedFollow> followers = flatFeed.getFollowers();
    }

    @Test
    public void shouldGetFollowing() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
        List<FeedFollow> following = flatFeed.getFollowing();
    }

    @Test
    public void shouldFollow() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
        flatFeed.follow("user:4");
    }

    @Test
    public void shouldUnfollow() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
        flatFeed.unfollow("user:4");
    }
}
