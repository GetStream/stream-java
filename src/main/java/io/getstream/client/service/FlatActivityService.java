package io.getstream.client.service;

import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;

public class FlatActivityService<T extends BaseActivity> extends AbstractActivityService<T> {
    public FlatActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }
}
