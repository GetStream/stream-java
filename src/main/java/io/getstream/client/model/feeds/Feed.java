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

public interface Feed {
	void follow(String targetFeedId) throws IOException, StreamClientException;

	void unfollow(String targetFeedId) throws IOException, StreamClientException;

	List<FeedFollow> getFollowers() throws IOException, StreamClientException;

	List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException;

	List<FeedFollow> getFollowing() throws IOException, StreamClientException;

	List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException;

    <T extends BaseActivity> AggregatedActivityService<T> newAggregatedActivityService(Class<T> clazz);

    <T extends BaseActivity> FlatActivityService<T> newFlatActivityService(Class<T> clazz);

    <T extends BaseActivity> UserActivityService<T> newUserActivityService(Class<T> clazz);

    <T extends BaseActivity> NotificationActivityService<T> newNotificationActivityService(Class<T> clazz);
}
