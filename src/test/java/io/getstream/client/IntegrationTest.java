package io.getstream.client;

import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.feeds.FlatFeed;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegrationTest {

    @Test
    public void shouldGetFollowers() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
		assertThat(flatFeed.getFollowers().size(), is(2));
    }

    @Test
    public void shouldGetFollowing() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
                                                            "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
        FeedFactory feedFactory = new FeedFactory(streamClient);

        FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
		assertThat(flatFeed.getFollowing().size(), is(0));
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

	@Test
	public void shouldGetActivities() throws IOException, StreamClientException {
		StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
				"245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
		FeedFactory feedFactory = new FeedFactory(streamClient);
		FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
		assertThat(flatFeed.getActivities().size(), is(0));
	}

	@Test
	public void shouldAddActivity() throws IOException, StreamClientException {
		StreamClient streamClient = new StreamClient(new ClientConfiguration(), "nfq26m3qgfyp",
				"245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");
		FeedFactory feedFactory = new FeedFactory(streamClient);
		FlatFeed flatFeed = feedFactory.createFlatFeed("user", "2");
		flatFeed.addActivity(new SimpleActivity());
	}
}
