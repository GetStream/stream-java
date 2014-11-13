package io.getstream.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.Nullable;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public abstract class BaseFeed<T extends BaseActivity> {

    protected final String feedSlug;

    protected final String userId;

    private final String id;

	@Nullable
	protected long maxLength = 0L;

	@Nullable
	protected boolean isRealtimeEnabled = false;

    @JsonIgnore
    private StreamRepository streamRepository;

    @JsonIgnore
    private Feed feedType;

    public BaseFeed(Feed feedType, StreamRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedType = feedType;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

    public void addActivity(T activity) throws SignatureException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        streamRepository.addActivity(this, activity);
    }

    public void addActivities(List<T> activity) {
    }

    public void remove() {

    }

    public void follow() {

    }

    public void unfollow() {

    }

    public List<String> getFollowers() {
        return null;
    }

    public List<String> getFollowing() {
        return null;
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
