package io.getstream.client.service;

import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.beans.StreamResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;

/**
 * Provide methods to interact with Flat activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public class FlatActivityServiceImpl<T extends BaseActivity> extends AbstractActivityService<T>
        implements FlatActivityService<T> {

    public FlatActivityServiceImpl(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }

    @Override
    public StreamResponse<T> getActivities() throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, new FeedFilter.Builder().build());
    }

    @Override
    public StreamResponse<T> getActivities(final FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getActivities(this.feed, type, filter);
    }
}
