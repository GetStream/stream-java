package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.service.AggregatedActivityService;
import io.getstream.client.service.FlatActivityService;
import io.getstream.client.service.NotificationActivityService;
import io.getstream.client.service.UserActivityService;

import java.io.IOException;
import java.util.List;

/**
 * Provide basic operation to perform against a feed.
 */
public class BaseFeed implements Feed {

	protected final StreamRepository streamRepository;
	protected final String feedSlug;
	protected final String userId;
	private final String id;

	/**
	 * Create a new feed using the given slug and user id.
	 * @param streamRepository Provide data repository to perform actual operation on a given feed.
	 *                         It must implement {@link StreamRepository}.
	 * @param feedSlug
	 * @param userId
	 */
    public BaseFeed(StreamRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

	@Override
	public void follow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.follow(this, targetFeedId);
    }

	@Override
	public void follow(List<String> targetFeedIds) throws IOException, StreamClientException {
		for (String targetFeedId : targetFeedIds) {
			streamRepository.follow(this, targetFeedId);
		}
	}

	@Override
	public void unfollow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.unfollow(this, targetFeedId);
    }

	@Override
	public void unfollow(List<String> targetFeedIds) throws IOException, StreamClientException {
		for (String targetFeedId : targetFeedIds) {
			streamRepository.unfollow(this, targetFeedId);
		}
	}

	@Override
	public List<FeedFollow> getFollowers() throws IOException, StreamClientException {
        return streamRepository.getFollowers(this, new FeedFilter.Builder().build());
    }

	@Override
	public List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException {
		return streamRepository.getFollowers(this, filter);
	}

	@Override
	public List<FeedFollow> getFollowing() throws IOException, StreamClientException {
        return streamRepository.getFollowing(this, new FeedFilter.Builder().build());
    }

	@Override
	public List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException {
		return streamRepository.getFollowing(this, filter);
	}

	@Override
	public void deleteActivity(String activityId) throws IOException, StreamClientException {
		streamRepository.deleteActivityById(this, activityId);
	}

	@Override
	public void deleteActivities(List<String> activityIds) throws IOException, StreamClientException {
		for (String activityId : activityIds) {
			streamRepository.deleteActivityById(this, activityId);
		}
	}

	@Override
	public <T extends BaseActivity> AggregatedActivityService<T> newAggregatedActivityService(Class<T> clazz) {
        return new AggregatedActivityService<>(this, clazz, streamRepository);
    }

	@Override
    public <T extends BaseActivity> FlatActivityService<T> newFlatActivityService(Class<T> clazz) {
        return new FlatActivityService<>(this, clazz, streamRepository);
    }

	@Override
    public <T extends BaseActivity> UserActivityService<T> newUserActivityService(Class<T> clazz) {
        return new UserActivityService<>(this, clazz, streamRepository);
    }

	@Override
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
