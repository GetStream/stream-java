package io.getstream.client.repo;

import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.model.beans.FeedFollow;

import java.io.IOException;
import java.util.List;

/**
 * Provide low-level access to the GetStream.io REST API.
 */
public interface StreamRepository {

	/**
	 * Delete activity by activity id.
	 * @param feed Feed that contains the activity to be deleted.
	 * @param activityId Activity-id to be deleted.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException;

	/**
	 * Follow a feed.
	 * @param feed Feed that wants to follow a target feed.
	 * @param targetFeedId Feed to follow.
	 * @throws StreamClientException
	 * @throws IOException
	 */
	void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

	/**
	 * Unfollow a feed.
	 * @param feed
	 * @param targetFeedId Feed to unfollow.
	 * @throws StreamClientException
	 * @throws IOException
	 */
	void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

	/**
	 * List the feeds which the given feed is following.
	 * @param feed
	 * @param filter Filter out the following list. Limited to 25 items by default.
	 * @return
	 * @throws StreamClientException
	 * @throws IOException
	 */
	List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

	/**
	 * Lists the followers for the given feed.
	 * @param feed
	 * @param filter Filter out the followers list. Limited to 25 items by default.
	 * @return
	 * @throws StreamClientException
	 * @throws IOException
	 */
	List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

	/**
	 * Lists the activities in the given feed.
	 * @param feed Feed which the activities belong to
	 * @param type Type of the activity. Must be a subtype of {@link BaseActivity}
	 * @param filter Filter out the activities. Limited to 25 items by default.
	 * @param <T>
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	<T extends BaseActivity> List<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

	/**
	 * Add a new activity to the given feed.
	 * @param feed Feed which the activities belong to
	 * @param activity Activity to add.
	 * @param <T>
	 * @return
	 * @throws StreamClientException
	 * @throws IOException
	 */
	<T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException;

	/**
	 * List aggregated activities.
	 * @param feed Feed which the activities belong to
	 * @param type Type of the activity. Must be a subtype of {@link BaseActivity}
	 * @param filter Filter out the activities. Limited to 25 items by default.
	 * @param <T>
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	<T extends BaseActivity> List<AggregatedActivity<T>> getAggregatedActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;
}
