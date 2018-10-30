package io.getstream.client.okhttp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.config.StreamRegion;
import io.getstream.client.exception.AuthenticationFailedException;
import io.getstream.client.exception.InvalidOrMissingInputException;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.activities.UpdateTargetResponse;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamActivitiesResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.beans.Targets;
import io.getstream.client.model.beans.UnfollowMany;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.AggregatedActivityServiceImpl;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.NotificationActivityServiceImpl;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.getstream.client.util.JwtAuthenticationUtil.ALL;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class IntegrationTest {

    public static final String API_KEY = "aygdeg2vhjxg";
    public static final String API_SECRET = "4vknf33hn4n94exgrg367jbmg4jxetem93bqcg3nkdf2xau3q8pmy3pftytq4w8v";
    public static final ClientConfiguration CLIENT_CONFIGURATION = new ClientConfiguration(StreamRegion.QA_TEST);

    public String getTestUserId(String userId) {
        long millis = System.currentTimeMillis();
        return String.format("%s_%d", userId, millis);
    }

    @Test
    public void shouldGetReadOnlyToken() throws StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);
        Feed feed = streamClient.newFeed("user", "1");

        Map<String, Claim> map = verifyToken(feed.getReadOnlyToken());
        assertTrue(map.size() > 0);
        assertThat(map.get("action").asString(), is("read"));
        assertThat(map.get("resource").asString(), is(ALL));
    }

    @Test
    public void shouldGetUserSessionToken() throws StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY, API_SECRET);
        Map<String, Claim> map = verifyToken(streamClient.getUserSessionToken("aUserId"));
        assertThat(map.get("user_id").asString(), is("aUserId"));
    }

    @Test
    public void shouldGetFollowers() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    public void shouldFollowWithActivityCopyLimit() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("shouldFollowOkHttp");
        Feed feed = streamClient.newFeed("user", followerId);

        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        assertThat(flatActivityService.getActivities().getResults().size(), is(0));

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        feed.follow("user", "1", 2);

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(1));

        assertThat(flatActivityService.getActivities().getResults().size(), is(2));

        streamClient.shutdown();
    }

    @Test
    public void shouldFollowMany() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("follower");
        Feed feed = streamClient.newFeed("user", followerId);

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        FollowMany followMany = new FollowMany.Builder()
                .add("user:" + followerId, "user:1")
                .add("user:" + followerId, "user:2")
                .add("user:" + followerId, "user:3")
                .build();
        feed.followMany(followMany);

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(3));

        FeedFilter filterPaged = new FeedFilter.Builder().withLimit(1).withOffset(1).build();
        List<FeedFollow> followingPaged = feed.getFollowing(filterPaged);
        assertThat(followingPaged.size(), is(1));

        FeedFilter filterByIds = new FeedFilter.Builder().withFeedIds(Arrays.asList("user:1", "user:2")).build();
        List<FeedFollow> followingIds = feed.getFollowing(filterByIds);
        assertThat(followingIds.size(), is(2));

        streamClient.shutdown();
    }

    @Test
    public void shouldUnfollowMany() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("shouldunfollowMany");
        Feed feed = streamClient.newFeed("user", followerId);

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        FollowMany followMany = new FollowMany.Builder()
                .add("user:" + followerId, "user:1")
                .add("user:" + followerId, "user:2")
                .add("user:" + followerId, "user:3")
                .build();
        feed.followMany(followMany);

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(3));

        UnfollowMany unfollowMany = new UnfollowMany.Builder()
                .add("user:" + followerId, "user:1")
                .add("user:" + followerId, "user:2", true)
                .add("user:" + followerId, "user:3", false)
                .build();
        feed.unfollowMany(unfollowMany);

        List<FeedFollow> unfollowingAfter = feed.getFollowing();
        assertThat(unfollowingAfter.size(), is(0));

        streamClient.shutdown();
    }

    @Test
    public void shouldFollowManyWithActivityCopyLimit() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String followerId = this.getTestUserId("follower");
        Feed feed = streamClient.newFeed("user", followerId);

        List<FeedFollow> following = feed.getFollowing();
        assertThat(following.size(), is(0));

        FollowMany followMany = new FollowMany.Builder()
                .add("user:" + followerId, "user:1")
                .add("user:" + followerId, "user:2")
                .add("user:" + followerId, "user:3")
                .build();
        feed.followMany(followMany, 50);

        List<FeedFollow> followingAfter = feed.getFollowing();
        assertThat(followingAfter.size(), is(3));

        FeedFilter filter = new FeedFilter.Builder().withLimit(1).withOffset(1).build();
        List<FeedFollow> followingPaged = feed.getFollowing(filter);
        assertThat(followingPaged.size(), is(1));

        streamClient.shutdown();
    }

    @Test
    public void shouldHaveOriginField() throws IOException, StreamClientException, InterruptedException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    public void shouldUnfollow() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        feed.unfollow("user", "3");
        feed.unfollow("user", "2", true); //keep history

        List<FeedFollow> followingAgain = feed.getFollowing();
        assertThat(followingAgain.size(), is(1));
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivities() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    public void shouldAddActivities() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldAddActivity");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");
        activity.setForeignId("foreign1");

        SimpleActivity activity2 = new SimpleActivity();
        activity2.setActor("actor");
        activity2.setObject("object");
        activity2.setTarget("target");
        activity2.setVerb("verb");
        activity2.setForeignId("foreign2");

        List<SimpleActivity> listToAdd = new ArrayList<>();
        listToAdd.add(activity);
        listToAdd.add(activity2);

        StreamActivitiesResponse<SimpleActivity> response = flatActivityService.addActivities(listToAdd);
        response.getActivities();

        streamClient.shutdown();
    }

    @Test
    public void shouldUpdateActivity() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldAddActivity");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setTime(new Date());
        activity.setForeignId("foreign1");
        activity.setVerb("verb");

        StreamActivitiesResponse<SimpleActivity> response = flatActivityService.updateActivities(Collections.singletonList(activity));
        response.getActivities();

        streamClient.shutdown();
    }

    @Test
    public void shouldUpdateToTargets() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String user1 = this.getTestUserId("shouldChangeTo1");
        Feed feed = streamClient.newFeed("user", user1);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService1 = feed.newFlatActivityService(SimpleActivity.class);

        SimpleActivity activityUser1 = new SimpleActivity();
        activityUser1.setForeignId("activityUser1");
        activityUser1.setActor("user1");
        activityUser1.setVerb("like");
        activityUser1.setObject("object1");
        activityUser1.setTime(new Date());

        /* add activity 'activityUser1' */
        flatActivityService1.addActivity(activityUser1);


        String user2 = this.getTestUserId("shouldChangeTo2");
        Feed feed2 = streamClient.newFeed("user", user2);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService2 = feed2.newFlatActivityService(SimpleActivity.class);

        SimpleActivity activityUser2 = new SimpleActivity();
        activityUser2.setForeignId("activityUser2");
        activityUser2.setActor("user2");
        activityUser2.setVerb("like");
        activityUser2.setObject("object1");
        activityUser2.setTime(new Date());

        /* add activity 'activityUser2' */
        flatActivityService2.addActivity(activityUser2);


        String user3 = this.getTestUserId("shouldChangeTo2");
        Feed feed3 = streamClient.newFeed("user", user3);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService3 = feed2.newFlatActivityService(SimpleActivity.class);

        SimpleActivity activityUser3 = new SimpleActivity();
        activityUser3.setForeignId("activityUser3");
        activityUser3.setActor("user3");
        activityUser3.setVerb("like");
        activityUser3.setObject("object1");
        activityUser3.setTime(new Date());
        activityUser3.setForeignId("user:".concat(user1));
        activityUser3.setTo(Collections.singletonList("user:".concat(user1))); // 'to' field points to 'user1' feed

        /* add activity 'activityUser3' with 'to' field pointing to feed user1 */
        flatActivityService3.addActivity(activityUser3);

        /* change the 'to' field of activity 'activityUser3' from user1 to user2 */
        /* addNewTarget(new_activity) replaces the 'to' field. It's equivalent to run addTargetToAdd(new_activity) and
         * addTargetToRemove(old_activity). See the below assertions. */
        Targets toTargets = new Targets.Builder()
                .addNewTarget("user:".concat(user2))
                .build();

        /* perform the change */
        UpdateTargetResponse<SimpleActivity> response = flatActivityService3.updateToTargets(activityUser3, toTargets);

        assertThat(response.getRemovedTargets(), hasItem("user:".concat(user1)));
        assertThat(response.getAddedTargets(), hasItem("user:".concat(user2)));
        assertThat(response.getActivity().getTo(), hasItem("user:".concat(user2)));

        streamClient.shutdown();
    }

    @Test
    public void shouldAddActivityToMany() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        String userId = this.getTestUserId("shouldAddActivity");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        SimpleActivity activity = new SimpleActivity();
        activity.setActor("actor");
        activity.setObject("object");
        activity.setTarget("target");
        activity.setVerb("verb");

        flatActivityService.addActivityToMany(
                ImmutableList.<String>of("user:1", "user:2").asList(),
                activity
        );
        streamClient.shutdown();
    }

    @Test
    public void shouldAddActivityToRecipients() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    public void shouldAddAndRetrieveActivityToRecipients() throws IOException, StreamClientException, InterruptedException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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

        /* needed to bypass the lock on backend side */
        Thread.sleep(3500);

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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);
        String userId = this.getTestUserId("shouldGetActivitiesWithFilter");
        Feed feed = streamClient.newFeed("user", userId);
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        flatActivityService.getActivities(new FeedFilter.Builder().withLimit(50).withOffset(2).build());
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesWithIdFilter() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    }

    @Test(expected = InvalidOrMissingInputException.class)
    public void shouldGetInvalidOrMissingInputException() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);

        Feed feed = streamClient.newFeed("foo", "2");
        FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);
        flatActivityService.getActivities(new FeedFilter.Builder().withLimit(50).withOffset(2).build());
        streamClient.shutdown();
    }

    @Test(expected = AuthenticationFailedException.class)
    public void shouldGetAuthenticationFailed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                "foo");

        Feed feed = streamClient.newFeed("user", "2");
        feed.follow("user", "4");
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromNotificationFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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

        /* updated marked seen and read */
        notificationActivityService.getActivities(new FeedFilter.Builder().build(), true, true);

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), false, true);
        assertThat((int)responseAfterMark.getUnread(), is(0));
        assertThat((int) responseAfterMark.getUnseen(), is(0));
        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromNotificationFeedAndMarkThemById() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfter =
                notificationActivityService.getActivities();
        assertThat((int)responseAfter.getUnread(), is(1));
        assertThat((int)responseAfter.getUnseen(), is(1));

        /* update marked read and seen */
        String aid = responseAfter.getResults().get(0).getId();
        MarkedActivity marker = new MarkedActivity.Builder().withActivityId(aid).build();
        notificationActivityService.getActivities(new FeedFilter.Builder().build(), marker, marker);

        StreamResponse<NotificationActivity<SimpleActivity>> responseAfterMark =
                notificationActivityService.getActivities(new FeedFilter.Builder().build(), new MarkedActivity.Builder().build(), marker);
        assertThat((int)responseAfterMark.getUnread(), is(0));
        assertThat((int)responseAfterMark.getUnseen(), is(0));

        streamClient.shutdown();
    }

    @Test
    public void shouldGetActivitiesFromEmptyAggregatedFeed() throws IOException, StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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
    public void shouldGetActivitiesFromAggregatedFeed() throws IOException, StreamClientException, InterruptedException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
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

        /* needed to bypass the lock on the backend */
        Thread.sleep(3500);

        aggregatedActivityService.addActivity(activity);
        List<AggregatedActivity<SimpleActivity>> oneActivityB = aggregatedActivityService.getActivities().getResults();
        assertThat(oneActivityB.size(), is(1));
        assertThat((int)oneActivityB.get(0).getActivityCount(), is(2));

        streamClient.shutdown();
    }

    @Test
    public void shouldHaveToken() throws StreamClientException {
        StreamClient streamClient = new StreamClientImpl(CLIENT_CONFIGURATION, API_KEY,
                API_SECRET);
        Feed feed = streamClient.newFeed("aggregated", "whatever");
        String token = feed.getToken();
        assertThat(token, notNullValue());
    }

    private Map<String, Claim> verifyToken(final String token) {
        byte[] secret = API_SECRET.getBytes();

        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaims();
    }
}
