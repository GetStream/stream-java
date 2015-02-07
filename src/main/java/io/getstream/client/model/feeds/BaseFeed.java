package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;
import java.util.List;

public abstract class BaseFeed {

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
