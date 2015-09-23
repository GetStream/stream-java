package io.getstream.client.okhttp;

import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.AggregatedActivityServiceImpl;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.NotificationActivityServiceImpl;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class IntegrationTest {

    public static final String API_KEY = "nfq26m3qgfyp";
    public static final String API_SECRET = "245nvvjm49s3uwrs5e4h3gadsw34mnwste6v3rdnd69ztb35bqspvq8kfzt9v7h2";

    //    @BeforeClass
    public static void setLog() {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "DEBUG");
    }

    public String getTestUserId(String userId) {
        long millis = System.currentTimeMillis();
        return String.format("%s_%d", userId, millis);
    }

    @Test
    public void shouldGetFollowers() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String followerId = this.getTestUserId("shouldGetFollowers");
        String followedId = this.getTestUserId("shouldGetFollowersFollowed");
        Feed feed = streamClient.newFeed("user", followerId);
        Feed followedFeed = streamClient.newFeed("user", followedId);

        List<FeedFollow> followers = followedFeed.getFollowers();
        assertThat(followers.size(), is(0));

        feed.follow("user", followedId);

        List<FeedFollow> followersAfter = followedFeed.getFollowers();
        assertThat(followersAfter.size(), is(1));

        streamClient.shutdown();
        streamClient.shutdown();
    }

    @Test
    public void shouldFollow() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("follower");
        Feed feed = streamClient.newFeed("user", followerId);

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        feed.follow("user", "1");
        feed.follow("user", "2");
        feed.follow("user", "3");

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(3));

        FeedFilter filter = new FeedFilter.Builder().withLimit(1).withOffset(1).build();
        List<FeedFollow> followingPaged = feed.getFollowing(filter);
        assertThat(followingPaged.size(), is(1));

        streamClient.shutdown();
    }

    @Test
    public void shouldHaveOriginField() throws IOException, StreamClientException, InterruptedException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String producerId = this.getTestUserId("shouldHaveOriginField1");
        Feed feedP = streamClient.newFeed("user", producerId);

        String consumerId = this.getTestUserId("shouldHaveOriginField2");
        Feed feedC = streamClient.newFeed("flat", consumerId);


        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feedP.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        flatActivityService.addActivity(activity);

        feedC.follow("user", producerId);
        TimeUnit.SECONDS.sleep(2);

        FlatActivityServiceImpl<SimpleActivity> flatActivityServiceC = feedC.newFlatActivityService(SimpleActivity.class);
        List<SimpleActivity> activities = flatActivityServiceC.getActivities().getResults();
        assertThat(activities.get(0).getOrigin(), is(feedP.getId()));
        streamClient.shutdown();
    }

    @Test
    public void shouldUnfollow() throws IOException, StreamClientException, InterruptedException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("follower");
        Feed feed = streamClient.newFeed("user", followerId);

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        feed.follow("user", "1");
        feed.follow("user", "2");
        feed.follow("user", "3");

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(3));
        feed.unfollow("user", "2");

        List<FeedFollow> followingAgain = feed.getFollowing();
        assertThat(followingAgain.size(), is(2));
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivities() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldGetActivities");
        Feed feed = streamClient.newFeed("user", userId);
        SimpleActivity activity = new SimpleActivity();
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        activity.setActor("alessandro");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        flatActivityService.addActivity(activity);
        for (SimpleActivity _activity : flatActivityService.getActivities().getResults()) {
            MatcherAssert.assertThat(_activity.getActor(), is("alessandro"));
        }
        streamClient.shutdown();
    }

    @Test
    public void shouldAddActivityWithTime() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldAddActivity");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setTime(new Date());
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        flatActivityService.addActivity(activity);
        streamClient.shutdown();
    }

    @Test
    public void shouldAddActivity() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldAddActivity");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        flatActivityService.addActivity(activity);
        streamClient.shutdown();
    }

    @Test
    public void shouldAddActivityToRecipients() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        Feed feed = streamClient.newFeed("user", "2");
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setTo(Arrays.asList("user:1", "user:4"));
        activity.setVerb("verb");
        flatActivityService.addActivity(activity);
        streamClient.shutdown();
    }

    @Test
    public void shouldAddAndRetrieveActivity() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldAddAndRetrieveActivityToRecipients");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        flatActivityService.addActivity(activity);
        List<SimpleActivity> secondRequest = flatActivityService.getActivities().getResults();
        assertThat(secondRequest.size(), is(1));
        streamClient.shutdown();
    }

    @Test
    public void shouldAddAndRetrieveActivityToRecipients() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldAddAndRetrieveActivityToRecipients");
        String recipientId1 = this.getTestUserId("shouldAddAndRetrieveActivityToRecipients1");
        String recipientId2 = this.getTestUserId("shouldAddAndRetrieveActivityToRecipients2");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setTo(Arrays.asList(String.format("user:%s", recipientId1), String.format("user:%s", recipientId2)));
        activity.setVerb("verb");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        flatActivityService.addActivity(activity);
        List<SimpleActivity> secondRequest = flatActivityService.getActivities().getResults();
        assertThat(secondRequest.size(), is(1));

        // retrieve the list of activities from the other 2 feeds too
        Feed feedRecipient1 = streamClient.newFeed("user", recipientId1);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService1 = feedRecipient1.newFlatActivityService(SimpleActivity.class);
        List<SimpleActivity> thirdRequest = flatActivityService1.getActivities().getResults();
        assertThat(thirdRequest.size(), is(1));

        Feed feedRecipient2 = streamClient.newFeed("user", recipientId2);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService2 = feedRecipient2.newFlatActivityService(SimpleActivity.class);
        List<SimpleActivity> forthRequest = flatActivityService2.getActivities().getResults();
        assertThat(forthRequest.size(), is(1));

        streamClient.shutdown();
    }

    @Test
    public void shouldReturnActivityIdAfterInsert() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldReturnActivityId");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        SimpleActivity response = flatActivityService.addActivity(activity);
        assertThat(response.getId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"), is(true));
        streamClient.shutdown();
    }

    @Test
    public void shouldActivityShouldHaveId() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldActivityShouldHaveId");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        flatActivityService.addActivity(activity);
        List<SimpleActivity> secondRequest = flatActivityService.getActivities().getResults();
        assertThat(secondRequest.size(), is(1));
        assertThat(secondRequest.get(0).getId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"), is(true));
        streamClient.shutdown();
    }

    @Test
    public void shouldRemoveActivity() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldRemoveActivity");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        SimpleActivity response = flatActivityService.addActivity(activity);
        List<SimpleActivity> secondRequest = flatActivityService.getActivities().getResults();
        assertThat(secondRequest.size(), is(1));

        List<SimpleActivity> thirdRequest = flatActivityService.getActivities().getResults();
        String aid = thirdRequest.get(0).getId();

        feed.deleteActivity(aid);
        List<SimpleActivity> forthRequest = flatActivityService.getActivities().getResults();
        assertThat(forthRequest.size(), is(0));

        streamClient.shutdown();
    }

    @Test
    public void shouldRemoveActivityByForeignId() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldRemoveActivity");

        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        activity.setForeignId("i2");
        List<SimpleActivity> firstRequest = flatActivityService.getActivities().getResults();
        assertThat(firstRequest.size(), is(0));
        SimpleActivity response = flatActivityService.addActivity(activity);
        List<SimpleActivity> secondRequest = flatActivityService.getActivities().getResults();
        assertThat(secondRequest.size(), is(1));

        List<SimpleActivity> thirdRequest = flatActivityService.getActivities().getResults();
        String aid = thirdRequest.get(0).getId();

        feed.deleteActivityByForeignId("i2");
        List<SimpleActivity> forthRequest = flatActivityService.getActivities().getResults();
        assertThat(forthRequest.size(), is(0));

        streamClient.shutdown();
    }
    @Test
    public void shouldGetActivitiesWithFilter() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldGetActivitiesWithFilter");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        flatActivityService.getActivities(new FeedFilter.Builder().withLimit(50).withOffset(2).build());
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesWithIdFilter() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldGetActivitiesWithIdFilter");
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        for(int i = 0; i < 5; i++ ) {
            flatActivityService.addActivity(activity);
        }
        List<SimpleActivity> activities = flatActivityService.getActivities().getResults();
        String aid = activities.get(2).getId();

        //lt
        List<SimpleActivity> lastTwoActivities = flatActivityService.getActivities(new FeedFilter.Builder().withIdLowerThan(aid).build()).getResults();
        assertThat(lastTwoActivities.size(), is(2));
        //lte
        List<SimpleActivity> lastThreeActivities = flatActivityService.getActivities(new FeedFilter.Builder().withIdLowerThanEquals(aid).build()).getResults();
        assertThat(lastThreeActivities.size(), is(3));
        //gt
        List<SimpleActivity> firstThreeActivities = flatActivityService.getActivities(new FeedFilter.Builder().withIdGreaterThanEquals(aid).build()).getResults();
        assertThat(firstThreeActivities.size(), is(3));
        //gte
        List<SimpleActivity> firstTwoActivities = flatActivityService.getActivities(new FeedFilter.Builder().withIdLowerThan(aid).build()).getResults();
        assertThat(firstTwoActivities.size(), is(2));
        //closed interval
        List<SimpleActivity> activity3 = flatActivityService.getActivities(new FeedFilter.Builder().withIdLowerThanEquals(aid).withIdGreaterThanEquals(aid).build()).getResults();
        assertThat(activity3.size(), is(1));
        //closed interval empty
        List<SimpleActivity> activityNo = flatActivityService.getActivities(new FeedFilter.Builder().withIdLowerThan(aid).withIdGreaterThan(aid).build()).getResults();
        assertThat(activityNo.size(), is(0));
        streamClient.shutdown();
    }

    @Test(expected = InvalidOrMissingInputException.class)
    public void shouldGetInvalidOrMissingInputException() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        Feed feed = streamClient.newFeed("foo", "2");
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        flatActivityService.getActivities(new FeedFilter.Builder().withLimit(50).withOffset(2).build());
        streamClient.shutdown();
    }

    @Test(expected = AuthenticationFailedException.class)
    public void shouldGetAuthenticationFailed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                "foo");

        Feed feed = streamClient.newFeed("user", "2");
        feed.follow("user", "4");
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromNotificationFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = getTestUserId("shouldGetActivitiesFromNotificationFeed");
        Feed feed = streamClient.newFeed("notification", userId);
        NotificationActivityServiceImpl<SimpleActivity> notificationActivityService =
                feed.newNotificationActivityService(SimpleActivity.class);
        StreamResponse<NotificationActivity<SimpleActivity>> response =
                notificationActivityService.getActivities(new FeedFilter.Builder().withLimit(50).withOffset(2).build(), true, true);
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromNotificationFeedAndMarkThem() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = getTestUserId("shouldGetActivitiesFromNotificationFeedAndMarkThem");
        Feed feed = streamClient.newFeed("notification", userId);
        NotificationActivityServiceImpl<SimpleActivity> notificationActivityService =
                feed.newNotificationActivityService(SimpleActivity.class);
        StreamResponse<NotificationActivity<SimpleActivity>> response =
                notificationActivityService.getActivities();
        assertThat((int)response.getUnread(), is(0));
        assertThat((int) response.getUnseen(), is(0));
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("like");
        notificationActivityService.addActivity(activity);
        StreamResponse<NotificationActivity<SimpleActivity>> responseAfter =
                notificationActivityService.getActivities();
        assertThat((int)responseAfter.getUnread(), is(1));
        assertThat((int) responseAfter.getUnseen(), is(1));

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), true, false);
        assertThat((int)responseAfterMark.getUnread(), is(0));
        assertThat((int) responseAfterMark.getUnseen(), is(1));

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark2 =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), false, true);
        assertThat((int)responseAfterMark2.getUnread(), is(0));
        assertThat((int) responseAfterMark2.getUnseen(), is(0));
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromNotificationFeedAndMarkThemById() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        String userId = getTestUserId("shouldGetActivitiesFromNotificationFeedAndMarkThemById");
        Feed feed = streamClient.newFeed("notification", userId);
        NotificationActivityServiceImpl<SimpleActivity> notificationActivityService =
                feed.newNotificationActivityService(SimpleActivity.class);
        StreamResponse<NotificationActivity<SimpleActivity>> response =
                notificationActivityService.getActivities();
        assertThat((int)response.getUnread(), is(0));
        assertThat((int) response.getUnseen(), is(0));
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("like");
        notificationActivityService.addActivity(activity);
        activity.setVerb("dislike");
        notificationActivityService.addActivity(activity);
        StreamResponse<NotificationActivity<SimpleActivity>> responseAfter =
                notificationActivityService.getActivities();
        assertThat((int)responseAfter.getUnread(), is(2));
        assertThat((int) responseAfter.getUnseen(), is(2));

        String aid = responseAfter.getResults().get(0).getId();
        MarkedActivity marker = new MarkedActivity.Builder().withActivityId(aid).build();
        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), marker, new MarkedActivity.Builder().build());
        assertThat((int)responseAfterMark.getUnread(), is(1));
        assertThat((int)responseAfterMark.getUnseen(), is(2));

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark2 =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), new MarkedActivity.Builder().build(), marker);
        assertThat((int)responseAfterMark2.getUnread(), is(1));
        assertThat((int)responseAfterMark2.getUnseen(), is(1));

        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromEmptyAggregatedFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("2");
        Feed feed = streamClient.newFeed("aggregated", userId);
        AggregatedActivityServiceImpl<SimpleActivity> aggregatedActivityService =
                feed.newAggregatedActivityService(SimpleActivity.class);
        List<AggregatedActivity<SimpleActivity>> noActivities = aggregatedActivityService.getActivities().getResults();
        assertThat(noActivities.size(), is(0));
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromAggregatedFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldGetActivitiesFromAggregatedFeed");
        Feed feed = streamClient.newFeed("aggregated", userId);
        AggregatedActivityServiceImpl<SimpleActivity> aggregatedActivityService =
                feed.newAggregatedActivityService(SimpleActivity.class);
        List<AggregatedActivity<SimpleActivity>> noActivities = aggregatedActivityService.getActivities().getResults();
        assertThat(noActivities.size(), is(0));

        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("like");

        aggregatedActivityService.addActivity(activity);
        List<AggregatedActivity<SimpleActivity>> oneActivity = aggregatedActivityService.getActivities().getResults();
        assertThat(oneActivity.size(), is(1));
        assertThat((int)oneActivity.get(0).getActivityCount(), is(1));

        aggregatedActivityService.addActivity(activity);
        List<AggregatedActivity<SimpleActivity>> oneActivityB = aggregatedActivityService.getActivities().getResults();
        assertThat(oneActivityB.size(), is(1));
        assertThat((int)oneActivityB.get(0).getActivityCount(), is(2));

        activity.setVerb("pin");
        aggregatedActivityService.addActivity(activity);
        List<AggregatedActivity<SimpleActivity>> twoActivities = aggregatedActivityService.getActivities().getResults();
        assertThat(twoActivities.size(), is(2));
        assertThat((int)twoActivities.get(0).getActivityCount(), is(1));
        assertThat((int)twoActivities.get(1).getActivityCount(), is(2));
        streamClient.shutdown();
    }

    @Test
    public void shouldHaveToken() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), API_KEY,
                API_SECRET);
        Feed feed = streamClient.newFeed("aggregated", "whatever");
        String token = feed.getToken();
        assertThat(token, notNullValue());
    }
}