package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;
import io.getstream.client.model.beans.StreamResponse;

import java.io.IOException;

/**
 * Provide methods to interact with Aggregated activities of subtype of {@link BaseActivity}.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
 */
public class AggregatedActivityServiceImpl<T extends BaseActivity> extends AbstractActivityService<T>
        implements AggregatedActivityService<T> {

    public AggregatedActivityServiceImpl(BaseFeed feed, Class<T> type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }

    @Override
    public StreamResponse<AggregatedActivity<T>> getActivities() throws IOException, StreamClientException {
        return streamRepository.getAggregatedActivities(this.feed, this.type, new FeedFilter.Builder().build());
    }

    @Override
    public StreamResponse<AggregatedActivity<T>> getActivities(final FeedFilter filter) throws IOException, StreamClientException {
        return streamRepository.getAggregatedActivities(this.feed, this.type, filter);
    }
}
