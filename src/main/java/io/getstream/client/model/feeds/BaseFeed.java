package io.getstream.client.model.feeds;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.service.StreamRepositoryRestImpl;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public abstract class BaseFeed<T extends BaseActivity> {

    protected final String feedSlug;

    protected final String userId;

    private final String id;

	//@Nullable
	protected long maxLength = 0L;

	//@Nullable
	protected boolean isRealtimeEnabled = false;

    @JsonIgnore
    private StreamRepositoryRestImpl streamRepository;

    @JsonIgnore
    private Feed feedType;

    public BaseFeed(Feed feedType, StreamRepositoryRestImpl streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedType = feedType;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

    public void addActivity(T activity) throws IOException, StreamClientException {
        streamRepository.addActivity(this, activity);
    }

    public void remove() {

    }

    public void follow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.follow(this, targetFeedId);
    }

    public void unfollow(String targetFeedId) throws IOException, StreamClientException {
        streamRepository.unfollow(this, targetFeedId);
    }

    public List<FeedFollow> getFollowers() throws IOException, StreamClientException {
        return streamRepository.getFollowers(this);
    }

    public List<FeedFollow> getFollowing() throws IOException, StreamClientException {
        return streamRepository.getFollowing(this);
    }

    public String getFeedSlug() {
        return feedSlug;
    }

    public String getUserId() {
        return userId;
    }

    public long getMaxLength() {

        return maxLength;
    }

    public boolean isRealtimeEnabled() {
        return isRealtimeEnabled;
    }

    public String getId() {
        return id;
    }
}
