package io.getstream.client.service;

import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;

/**
 * Provide methods to interact with User activities of subtype of {@link BaseActivity}.
 * @param <T>
 */
public class UserActivityService<T extends BaseActivity> extends AbstractActivityService<T> {
    public UserActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        super(feed, type, streamRepository);
    }
}


