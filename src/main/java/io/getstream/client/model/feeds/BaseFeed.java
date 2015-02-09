package io.getstream.client.model.feeds;

import io.getstream.client.service.AggregatedActivityService;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.service.FlatActivityService;
import io.getstream.client.service.NotificationActivityService;
import io.getstream.client.service.UserActivityService;

import java.io.IOException;
import java.util.List;

public class BaseFeed implements Feed {

	protected final StreamRepository streamRepository;
	protected final String feedSlug;
	protected final String userId;
	private final String id;

    public BaseFeed(StreamRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

	public void follow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.follow(this, targetFeedId);
    }

	public void unfollow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.unfollow(this, targetFeedId);
    }

	public List<FeedFollow> getFollowers() throws IOException, StreamClientException {
        return streamRepository.getFollowers(this, new FeedFilter.Builder().build());
    }

	public List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException {
		return streamRepository.getFollowers(this, filter);
	}

	public List<FeedFollow> getFollowing() throws IOException, StreamClientException {
        return streamRepository.getFollowing(this, new FeedFilter.Builder().build());
    }

	public List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException {
		return streamRepository.getFollowing(this, filter);
	}

    public <T extends BaseActivity> AggregatedActivityService<T> newAggregatedActivityService(Class<T> clazz) {
        return new AggregatedActivityService<>(this, clazz, streamRepository);
    }

    public <T extends BaseActivity> FlatActivityService<T> newFlatActivityService(Class<T> clazz) {
        return new FlatActivityService<>(this, clazz, streamRepository);
    }

    public <T extends BaseActivity> UserActivityService<T> newUserActivityService(Class<T> clazz) {
        return new UserActivityService<>(this, clazz, streamRepository);
    }

    public <T extends BaseActivity> NotificationActivityService<T> newNotificationActivityService(Class<T> clazz) {
        return new NotificationActivityService<>(this, clazz, streamRepository);
    }

    public String getFeedSlug() {
        return feedSlug;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }
}
