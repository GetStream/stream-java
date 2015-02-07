package io.getstream.client.model.activities;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;
import java.util.List;

public abstract class AbstractActivityMediator<T extends BaseActivity> {
    protected final Class<T> type;
    protected final BaseFeed feed;
    protected final StreamRepository streamRepository;

    public AbstractActivityMediator(BaseFeed feed, Class type, StreamRepository streamRepository) {
        this.type = type;
        this.feed = feed;
        this.streamRepository = streamRepository;
    }

    public T addActivity(T activity) throws IOException, StreamClientException {
        return streamRepository.addActivity(this.feed, activity);
    }

    public List<T> getActivities() throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, new FeedFilter.Builder().build());
    }

    public List<T> getActivities(final FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, filter);
    }
}
