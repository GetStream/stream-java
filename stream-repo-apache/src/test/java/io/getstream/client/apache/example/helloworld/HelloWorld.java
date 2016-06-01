package io.getstream.client.apache.example.helloworld;

import io.getstream.client.StreamClient;
import io.getstream.client.apache.StreamClientImpl;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.service.FlatActivityServiceImpl;

import java.io.IOException;
import java.util.Arrays;

/**
 * The following example creates a new custom activity and push it into the feed.
 */
public class HelloWorld {

    public static void main(String[] args) throws IOException, StreamClientException {
        /**
         * Create client using api key and secret key.
         */
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), "nfq26m3qgfyp",
                "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2");

        /**
         * Get the referent to a feed (either new or existing one).
         */
        Feed feed = streamClient.newFeed("user", "2");

        /**
         * Create new custom activity.
         */
        HelloWorldActivity helloWorld = new HelloWorldActivity();
        helloWorld.setActor("Me");
        helloWorld.setObject("Message");
        helloWorld.setTarget("");
        helloWorld.setTo(Arrays.asList("user:1"));
        helloWorld.setVerb("verb");
        helloWorld.setMessage("Hello World!");

        /**
         * Create a service to get/add HelloWorldActivities over a FlatFeed.
         */
        FlatActivityServiceImpl<HelloWorldActivity> flatActivityService = feed.newFlatActivityService(HelloWorldActivity.class);
        flatActivityService.addActivity(helloWorld);

        /**
         * Shutdown the client.
         */
        streamClient.shutdown();
    }

    /**
     * A custom activity must extends BaseActivity.
     * Feel free to add as many fields as you want.
     */
    static class HelloWorldActivity extends BaseActivity {
        protected String message;

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
