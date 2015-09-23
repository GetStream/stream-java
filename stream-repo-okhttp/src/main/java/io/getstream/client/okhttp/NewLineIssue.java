package io.getstream.client.okhttp;

import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.okhttp.repo.utils.StreamRepoUtils;
import io.getstream.client.service.NotificationActivityServiceImpl;

public class NewLineIssue {
    public static void main(String[] args) throws Exception {
        ClientConfiguration streamConfig = new ClientConfiguration();
        streamConfig.setRegion(StreamRegion.EU_WEST);

        StreamClient streamClient = new StreamClientImpl(streamConfig, "22zypf3ftsq6", "anuduh82hmh488vxcsjnzcfpfbtbn9826akpjt954v2usf8d2uz9nh36tpm9xgns");
        Feed feed = streamClient.newFeed("notification", "523b3891e2c70d5d81622fcc");

        String signature = StreamRepoUtils.createFeedSignature((BaseFeed) feed, "anuduh82hmh488vxcsjnzcfpfbtbn9826akpjt954v2usf8d2uz9nh36tpm9xgns");
        System.out.println("START");
        System.out.println(signature);
        System.out.println("END");
        if (signature.contains("\n")) {
            System.out.println("New line detected");
        }
        NotificationActivityServiceImpl<SimpleActivity> activityService =
                feed.newNotificationActivityService(SimpleActivity.class);

        SimpleActivity activity = new SimpleActivity();
        //activity.setActor("Account_523b3891e2c70d5d81622fcc_Project_523b3891e2c70d5d81622fcc");
        activity.setVerb("test");
        activity.setObject("523b3891e2c70d5d81622fcc");
        activityService.addActivity(activity);

    }
}
