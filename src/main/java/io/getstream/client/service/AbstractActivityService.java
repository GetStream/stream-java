package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

/**
 * Provides operations to be performed against activities.
 * @param <T>
 */
public abstract class AbstractActivityService<T extends BaseActivity> implements ActivityService<T> {
    protected final Class<T> type;
    protected final BaseFeed feed;
    protected final StreamRepository streamRepository;

    public AbstractActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        this.type = type;
        this.feed = feed;
        this.streamRepository = streamRepository;
    }

    @Override
    public T addActivity(T activity) throws IOException, StreamClientException {
        return streamRepository.addActivity(this.feed, activity);
    }

    @Override
    public List<T> getActivities() throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, new FeedFilter.Builder().build());
    }

    @Override
    public List<T> getActivities(final FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, filter);
    }
}
