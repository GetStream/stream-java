package io.getstream.client.apache.example.follow;

import io.getstream.client.apache.StreamClientImpl;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;

/**
 * The following example plays with following/followers.
 */
public class Follow {

    public static void main(String[] args) throws IOException, StreamClientException {
        /**
         * Create client using api key and secret key.
         */
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), "nfq26m3qgfyp",
                                                                "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");

        /**
         * Get the reference to a feed (either new or existing one).
         */
        Feed feed = streamClient.newFeed("user", "2");

        /**
         * Start following some feeds.
         */
        feed.follow("user", "3");
        feed.follow("user", "4");
        feed.follow("user", "5");

        /**
         * Print out the list of following.
         */
        for (FeedFollow feedFollow : feed.getFollowing()) {
            System.out.println(feedFollow.getCreatedAt() + " " + feedFollow.getTargetId());
        }

        /**
         * Print out the list of followers, instead.
         */
        for (FeedFollow feedFollow : feed.getFollowers()) {
            System.out.println(feedFollow.getCreatedAt() + " " + feedFollow.getFeedId());
        }

        /**
         * Print out a shorter list of followers, instead.
         */
        for (FeedFollow feedFollow : feed.getFollowers(new FeedFilter.Builder().withLimit(1).build())) {
            System.out.println(feedFollow.getCreatedAt() + " " + feedFollow.getFeedId());
        }

        /**
         * Shutdown the client.
         */
        streamClient.shutdown();
    }
}
