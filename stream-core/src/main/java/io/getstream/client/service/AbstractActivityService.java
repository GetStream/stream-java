package io.getstream.client.service;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.model.beans.StreamActivitiesResponse;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.repo.StreamRepository;

import java.io.IOException;
import java.util.List;

/**
 * Provides operations to be performed against activities.
 *
 * @param <T> Subtype of {@link BaseActivity} representing the activity type to handle.
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
     *
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.<br>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    public T addActivity(T activity) throws IOException, StreamClientException {
        return streamRepository.addActivity(this.feed, activity);
    }

    /**
     * Add a new activity of type {@link T}.
     *
     * @param activities List of Activities to add.
     * @return Response activity of type {@link T} coming from the server.<br>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    public StreamActivitiesResponse<T> addActivities(List<T> activities) throws IOException, StreamClientException {
        return streamRepository.addActivities(this.feed, type, activities);
    }

    /**
     * Add a new activity of type {@link T}.
     *
     * @param activities List of activities to update
     * @return Response activity of type {@link T} coming from the server.<br>
     *         The returning activity in the 'to' field contains the targetFeedId along
     *         with its signature (e.g: 'user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac')
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    public StreamActivitiesResponse<T> updateActivities(List<T> activities) throws IOException, StreamClientException {
        return streamRepository.updateActivities(this.feed, type, activities);
    }

    /**
     * Add a new activity of type {@link T} to multiple feeds.
     *
     * @param targetIds Destination feeds. A target id is defined as $feedSlug:$feedId.
     * @param activity Activity to add.
     * @return Response activity of type {@link T} coming from the server.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    public T addActivityToMany(List<String> targetIds, T activity) throws IOException, StreamClientException {
        return streamRepository.addActivityToMany(targetIds, activity);
    }
}
