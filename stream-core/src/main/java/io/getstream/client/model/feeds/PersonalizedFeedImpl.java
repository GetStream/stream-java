package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.repo.StreamPersonalizedRepository;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Mediator class to interact with the actual repository.
 */
public class PersonalizedFeedImpl implements PersonalizedFeed {

    protected final StreamPersonalizedRepository streamRepository;
    private String slug;
    private String userId;

    public PersonalizedFeedImpl(StreamPersonalizedRepository streamRepository, String feedSlug, String userId) {
        this.streamRepository = streamRepository;
        this.slug = feedSlug;
        this.userId = userId;
    }

    @Override
    public <T extends PersonalizedActivity> List<T> get(Class<T> type, FeedFilter filter) throws IOException, StreamClientException {
        return this.streamRepository.get(this, type, filter);
    }

    @Override
    public MetaResponse addMeta(PersonalizedFeed feed, Serializable metaPayload) throws IOException, StreamClientException {
        return this.streamRepository.addMeta(this, metaPayload);
    }

    @Override
    public <T extends Serializable> List<T> getInterest(Class<T> type) throws IOException, StreamClientException {
        return this.streamRepository.getInterest(this, type);
    }

    public String getSlug() {
        return slug;
    }

    public String getUserId() {
        return userId;
    }
}
