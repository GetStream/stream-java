package io.getstream.client.repo;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.activities.NotificationActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.beans.StreamActivitiesResponse;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.util.List;

/**
 * Provide low-level access to the GetStream.io REST API.
 */
public interface StreamRepository {

    /**
     * Delete activity by activity id.
     *
     * @param feed       Feed that contains the activity to be deleted.
     * @param activityId Activity-id to be deleted.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException;

    /**
     * Delete activity by foreign id.
     *
     * @param feed       Feed that contains the activity to be deleted.
     * @param foreignId foreignId to be deleted.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exceptions
     */
    void deleteActivityByForeignId(BaseFeed feed, String foreignId) throws IOException, StreamClientException;

    /**
     * Generate a JWT token to perform readonly operations.
     * @param feed Input feed
     * @return JWT token
     */
    String getReadOnlyToken(BaseFeed feed);

    /**
     * Follow a feed.
     *
     * @param feed         Feed that wants to follow a target feed.
     * @param targetFeedId Feed to follow.
     * @param activityCopyLimit How many activities should be copied from the target feed
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    void follow(BaseFeed feed, String targetFeedId, int activityCopyLimit) throws StreamClientException, IOException;

    /**
     * Follow many feed in one shot.
     *
     * @param feed Feed that wants to follow a target feed.
     * @param followManyInput A {@link FollowMany} object which contains a list of sources and targets
     * @param activityCopyLimit Number of activities to copy from a source feed to the destination feed
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    void followMany(BaseFeed feed, FollowMany followManyInput, int activityCopyLimit) throws StreamClientException, IOException;

    /**
     * Unfollow a feed.
     *
     * @param feed Source feed
     * @param targetFeedId Feed to unfollow.
     * @param keepHistory Whether the activities from the unfollowed feed should be removed
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    void unfollow(BaseFeed feed, String targetFeedId, boolean keepHistory) throws StreamClientException, IOException;

    /**
     * List the feeds which the given feed is following.
     *
     * @param feed Source feed
     * @param filter Filter out the following list. Limited to 25 items by default.
     * @return List of following
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

    /**
     * Lists the followers for the given feed.
     *
     * @param feed Source feed
     * @param filter Filter out the followers list. Limited to 25 items by default.
     * @return List of followers
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

    /**
     * Lists the activities in the given feed.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter Filter out the activities. Limited to 25 items by default.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return List of activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamResponse<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * Add a new activity to the given feed.
     *
     * @param feed     Feed which the activities belong to
     * @param activity Activity to add.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return Activity as returned by the Stream server
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    <T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException;

    /**
     * Add a new list of activities to the given feed.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param feed     Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param activities List of activities to add  @return Activity as returned by the Stream server
     * @return List of just sent activities
     * @throws StreamClientException in case of functional or server-side exception
     * @throws IOException in case of network/socket exceptions
     */
    <T extends BaseActivity> StreamActivitiesResponse<T> addActivities(BaseFeed feed, Class<T> type, List<T> activities) throws StreamClientException, IOException;

    /**
     * Update activities (foreignId and time are mandatory fields).
     * Please refer to GetStream.io/docs for more info.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param feed Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param activities List of activities to update  @return Operation response
     * @return List of just sent activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamActivitiesResponse<T> updateActivities(BaseFeed feed, Class<T> type, List<T> activities) throws IOException, StreamClientException;

    /**
     * Add a new activity of type {@link T} to multiple feeds.
     *
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @param targetIds Destination feeds. A target id is defined as $feedSlug:$feedId.
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> T addActivityToMany(List<String> targetIds, T activity) throws StreamClientException, IOException;

    /**
     * List aggregated activities.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter Filter out the activities. Limited to 25 items by default.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return List of aggregated activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamResponse<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List notification activities.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter Filter out the activities.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return List of notification activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * List notifications marking the activities as read and/or as seen.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter     Filter the activities.
     * @param markAsRead Mark all the activities as read.
     * @param markAsSeen Mark all the activities as seen.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return List of notification activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, boolean markAsRead, boolean markAsSeen) throws IOException, StreamClientException;

    /**
     * List notifications marking some of them as read and/or as seen.
     * An immutable list of activities to be marked as read and/or as seen can be
     * built using the {@link MarkedActivity} builder.
     *
     * @param feed   Feed which the activities belong to
     * @param type   Type of the activity. Must be a subtype of {@link BaseActivity}
     * @param filter     Filter the activities.
     * @param markAsRead List of activities to be marked as read.
     * @param markAsSeen List of activities to be marked as seen.
     * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
     * @return List of notification activities
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends BaseActivity> StreamResponse<NotificationActivity<T>> getNotificationActivities(BaseFeed feed, Class<T> type, FeedFilter filter, MarkedActivity markAsRead, MarkedActivity markAsSeen) throws IOException, StreamClientException;

    /**
     * Get the token for the given feed.
     * @param feed Feed
     * @return A token string
     */
    String getToken(BaseFeed feed);

    /**
     * Send the shutdown signal to the client.
     *
     * @throws IOException in case of network/socket exceptions
     */
    void shutdown() throws IOException;
}
