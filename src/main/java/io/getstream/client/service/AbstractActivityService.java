package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;

/**
 * Provides operations to be performed against activities.
 * @param <T>
 */
public abstract class AbstractActivityService<T extends BaseActivity> {
    protected Class<T> type;
    protected final BaseFeed feed;
    protected final StreamRepository streamRepository;

    public AbstractActivityService(BaseFeed feed, Class type, StreamRepository streamRepository) {
        this.type = type;
        this.feed = feed;
        this.streamRepository = streamRepository;
    }

    /**
     * Add a new activity of type {@link T}.
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.<br/>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException
     * @throws StreamClientException
     */
    public T addActivity(T activity) throws IOException, StreamClientException {
        return streamRepository.addActivity(this.feed, activity);
    }
}
