package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;
import java.util.List;

public class AggregatedFeed extends BaseFeed implements Feed {
    public AggregatedFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(streamRepository, feedSlug, userId);
    }

	public class AggregatedActivity<T extends BaseActivity> extends ActivityBuilder<T> {
		public AggregatedActivity(Class type) {
			super(type);
		}

		public List<T> getActivities(final boolean markAsRead) throws IOException, StreamClientException {
			return streamRepository.getActivities(AggregatedFeed.this, type, new FeedFilter.Builder().build());
		}

		public List<T> getActivities(final FeedFilter filter, final boolean markAsRead) throws IOException, StreamClientException {
			return streamRepository.getActivities(AggregatedFeed.this, type, filter);
		}
	}
}
