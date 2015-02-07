package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.AbstractActivityMediator;
import io.getstream.client.model.activities.ActivityMediatorProvider;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.StreamRepository;

import java.io.IOException;
import java.util.List;

public class AggregatedFeed extends BaseFeed implements Feed, ActivityMediatorProvider {
    public AggregatedFeed(final StreamRepository streamRepository, String feedSlug, String userId) {
        super(streamRepository, feedSlug, userId);
    }

    @Override
    public <T extends BaseActivity> ActivityMediator<T> newActivityMediator(Class<T> clazz) {
        return new ActivityMediator<>(this, clazz, streamRepository);
    }

    public static class ActivityMediator<T extends BaseActivity> extends AbstractActivityMediator<T> {
        public ActivityMediator(BaseFeed feed, Class type, StreamRepository streamRepository) {
            super(feed, type, streamRepository);
        }

        public List<T> getActivities(final boolean markAsRead) throws IOException, StreamClientException {
            return streamRepository.getActivities(this.feed, type, new FeedFilter.Builder().build());
        }

        public List<T> getActivities(final FeedFilter filter, final boolean markAsRead) throws IOException, StreamClientException {
            return streamRepository.getActivities(this.feed, type, filter);
        }
    }
}