package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.MarkedActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

public class AggregatedActivityService<T extends BaseActivity> extends AbstractActivityService<T> {

    public AggregatedActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }

    public List<T> getActivities(final boolean markAsRead) throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, new FeedFilter.Builder().build());
    }
}
