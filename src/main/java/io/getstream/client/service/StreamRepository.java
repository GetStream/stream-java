package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.model.bean.FeedFollow;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface StreamRepository {

	void deleteActivityById(BaseFeed feed, String activityId) throws IOException, StreamClientException;

	void follow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

	void unfollow(BaseFeed feed, String targetFeedId) throws StreamClientException, IOException;

	List<FeedFollow> getFollowing(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

	List<FeedFollow> getFollowers(BaseFeed feed, FeedFilter filter) throws StreamClientException, IOException;

	<T extends BaseActivity> List<T> getActivities(BaseFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

	<T extends BaseActivity> T addActivity(BaseFeed feed, T activity) throws StreamClientException, IOException;
}
