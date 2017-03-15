package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.FeedFollow;
import io.getstream.client.model.beans.FollowMany;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.service.AggregatedActivityServiceImpl;
import io.getstream.client.service.FlatActivityServiceImpl;
import io.getstream.client.service.NotificationActivityServiceImpl;
import io.getstream.client.service.UserActivityServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * Provide basic operation to perform against a feed.
 */
public class BaseFeed implements Feed {

    private static final int DEFAULT_ACTIVITY_COPY_LIMIT = 300;
    private static final boolean DEFAULT_KEEP_HISTORY = false;

    protected final StreamRepository streamRepository;
    protected final String feedSlug;
    protected final String userId;
    private final String id;

    /**
     * Create a new feed using the given slug and user id.
     *
     * @param streamRepository Provide data repository to perform actual operation on a given feed.
     *                         It must implement {@link StreamRepository}.
     * @param feedSlug Feed slug
     * @param userId User ID
     */
    public BaseFeed(StreamRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.feedSlug = feedSlug;
        this.userId = userId;
        this.id = feedSlug.concat(":").concat(userId);
    }

    @Override
    public String getReadOnlyToken() {
        return streamRepository.getReadOnlyToken(this);
    }

    @Override
    public void follow(String feedSlug, String userId) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.follow(this, feedId, DEFAULT_ACTIVITY_COPY_LIMIT);
    }

    @Override
    public void follow(String feedSlug, String userId, int activityCopyLimit) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.follow(this, feedId, activityCopyLimit);
    }

    @Override
    public void followMany(FollowMany follows, int activityCopyLimit) throws IOException, StreamClientException {
        streamRepository.followMany(this, follows, activityCopyLimit);
    }

    @Override
    public void followMany(FollowMany follows) throws IOException, StreamClientException {
        streamRepository.followMany(this, follows, DEFAULT_ACTIVITY_COPY_LIMIT);
    }

    @Override
    public void unfollow(String feedSlug, String userId) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.unfollow(this, feedId, DEFAULT_KEEP_HISTORY);
    }

    @Override
    public void unfollow(String feedSlug, String userId, boolean keepHistory) throws IOException, StreamClientException {
        String feedId = String.format("%s:%s", feedSlug, userId);
        streamRepository.unfollow(this, feedId, keepHistory);
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
    public void deleteActivityByForeignId(String foreignId) throws IOException, StreamClientException {
        streamRepository.deleteActivityByForeignId(this, foreignId);
    }

    @Override
    public <T extends BaseActivity> AggregatedActivityServiceImpl<T> newAggregatedActivityService(Class<T> clazz) {
        return new AggregatedActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> FlatActivityServiceImpl<T> newFlatActivityService(Class<T> clazz) {
        return new FlatActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> UserActivityServiceImpl<T> newUserActivityService(Class<T> clazz) {
        return new UserActivityServiceImpl<>(this, clazz, streamRepository);
    }

    @Override
    public <T extends BaseActivity> NotificationActivityServiceImpl<T> newNotificationActivityService(Class<T> clazz) {
        return new NotificationActivityServiceImpl<>(this, clazz, streamRepository);
    }

    public String getFeedSlug() {
        return feedSlug;
    }

    public String getFeedId() {
        return getFeedSlug().concat(getUserId());
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getToken() {
        return streamRepository.getToken(this);
    }
}
