package io.getstream.client.model.feeds;

import io.getstream.client.model.activities.AbstractActivityMediator;
import io.getstream.client.model.activities.ActivityMediatorProvider;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.service.StreamRepository;

public class FlatFeed extends BaseFeed implements Feed, ActivityMediatorProvider {
    public FlatFeed(StreamRepository streamRepository, String feedSlug, String id) {
        super(streamRepository, feedSlug, id);
    }

    @Override
    public <T extends BaseActivity> ActivityMediator<T> newActivityMediator(Class<T> clazz) {
        return new ActivityMediator<>(this, clazz, streamRepository);
    }

    public static class ActivityMediator<T extends BaseActivity> extends AbstractActivityMediator<T> {
        public ActivityMediator(BaseFeed feed, Class type, StreamRepository streamRepository) {
            super(feed, type, streamRepository);
        }
    }
}
