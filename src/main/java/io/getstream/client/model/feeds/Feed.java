package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.AggregatedActivityService;
import io.getstream.client.service.FlatActivityService;
import io.getstream.client.service.NotificationActivityService;
import io.getstream.client.service.UserActivityService;

import java.io.IOException;
import java.util.List;

/**
 * Feed class. It exposes operations to perform against a feed.
 */
public interface Feed {

	/**
	 * Follows the given target feed.
	 * @param targetFeedId the slug of the target feed.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void follow(String targetFeedId) throws IOException, StreamClientException;

	/**
	 * Follow the given target feeds.
	 * @param targetFeedIds a list of target feeds to follow.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void follow(List<String> targetFeedIds) throws IOException, StreamClientException;

	/**
	 * Unfollow the given target feed.
	 * @param targetFeedId the slug of the target feed.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void unfollow(String targetFeedId) throws IOException, StreamClientException;

	/**
	 * Unfollow the given target feeds.
	 * @param targetFeedIds a list of target feeds to unfollow.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void unfollow(List<String> targetFeedIds) throws IOException, StreamClientException;

	/**
	 * Lists the followers of the feed.
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	List<FeedFollow> getFollowers() throws IOException, StreamClientException;

	/**
	 * Lists the followers of the feed using the given filter.
	 * @param filter Filter out the followers.
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException;

	/**
	 * List the feeds which this feed is following.
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	List<FeedFollow> getFollowing() throws IOException, StreamClientException;

	/**
	 * List the feeds which this feed is following using the give filter.
	 * @param filter Filter out the list of following feeds.
	 * @return
	 * @throws IOException
	 * @throws StreamClientException
	 */
	List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException;

	/**
	 * Removes an activity from the feed.
	 * @param activityId the activity id to remove from this feed.
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void deleteActivity(String activityId) throws IOException, StreamClientException;

	/**
	 * Removes a list of activities from the feed.
	 * It is not executed in batch fashion.
	 * @param activityIds
	 * @throws IOException
	 * @throws StreamClientException
	 */
	void deleteActivities(List<String> activityIds) throws IOException, StreamClientException;

	/**
	 * Get mediator service to handle aggregated activities.
	 * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
	 * @param <T>
	 * @return
	 */
    <T extends BaseActivity> FlatActivityService<T> newFlatActivityService(Class<T> clazz);

	/**
	 * Get mediator service to handle aggregated activities.
	 *
	 * @param clazz Subtype of {@link io.getstream.client.model.activities.BaseActivity} representing the activity type to handle.
	 * @return
	 */
	<T extends BaseActivity> AggregatedActivityService<T> newAggregatedActivityService(Class<T> clazz);

	/**
	 * Get mediator service to handle aggregated activities.
	 * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
	 * @param <T>
	 * @return
	 */
    <T extends BaseActivity> UserActivityService<T> newUserActivityService(Class<T> clazz);

	/**
	 * Get mediator service to handle aggregated activities.
	 * @param clazz Subtype of {@link BaseActivity} representing the activity type to handle.
	 * @param <T>
	 * @return
	 */
    <T extends BaseActivity> NotificationActivityService<T> newNotificationActivityService(Class<T> clazz);
}
