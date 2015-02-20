package io.getstream.client.service;

import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

/**
 * Provide methods to interact with Aggregated activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public class AggregatedActivityService<T extends BaseActivity> extends AbstractActivityService<T> {

    public AggregatedActivityService(BaseFeed feed, Class<T> type, StreamRepository streamRepository) {
		super(feed, type, streamRepository);
    }

    public List<AggregatedActivity<T>> getAggregatedActivities() throws IOException, StreamClientException {
        return streamRepository.getAggregatedActivities(this.feed, this.type, new FeedFilter.Builder().build());
    }

	public List<AggregatedActivity<T>> getAggregatedActivities(final FeedFilter filter) throws IOException, StreamClientException {
		return streamRepository.getAggregatedActivities(this.feed, this.type, filter);
	}
}
