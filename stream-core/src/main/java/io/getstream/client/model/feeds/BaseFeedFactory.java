package io.getstream.client.model.feeds;

import com.google.common.base.Optional;
import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.repo.StreamPersonalizedRepository;
import io.getstream.client.repo.StreamRepository;

import java.util.regex.Pattern;

/**
 * Factory class to build a new instance of a feed.
 */
public final class BaseFeedFactory implements FeedFactory {

    private final static Pattern FEED_SLUG_PATTERN = Pattern.compile(FEED_SLUG_ALLOWED_PATTERN);
    private final static Pattern FEED_ID_PATTERN = Pattern.compile(FEED_ID_ALLOWED_PATTERN);

    private final StreamRepository streamRepository;
    private final Optional<StreamPersonalizedRepository> streamPersonalizedRepository;

    /**
     * Build a FeedFactory by giving the actual implementation of the repositories.
     * @param streamRepository Repository for Stream API
     * @param streamPersonalizedRepository Repository for Personalized API
     */
    public BaseFeedFactory(final StreamRepository streamRepository,
                           final Optional<StreamPersonalizedRepository> streamPersonalizedRepository) {
        this.streamRepository = streamRepository;
        this.streamPersonalizedRepository = streamPersonalizedRepository;
    }

    @Override
    public Feed createFeed(final String feedSlug, final String id) throws InvalidFeedNameException {
        if (FEED_SLUG_PATTERN.matcher(feedSlug).matches() && FEED_ID_PATTERN.matcher(id).matches()) {
            return new BaseFeed(streamRepository, feedSlug, id);
        }
        throw new InvalidFeedNameException("Either feedSlug or id are not valid. Feed slug only accept words, feed id accepts words and hyphens");
    }

    @Override
    public PersonalizedFeed createPersonalizedFeed(final String feedSlug, final String id) throws InvalidFeedNameException {
        if (!streamPersonalizedRepository.isPresent()) {
            throw new UnsupportedOperationException("Personalized feed not properly initialized." +
                    "Please provide a working endpoint for Personalization.");
        }
        if (FEED_SLUG_PATTERN.matcher(feedSlug).matches() && FEED_ID_PATTERN.matcher(id).matches()) {
            return new PersonalizedFeedImpl(streamPersonalizedRepository.get(), feedSlug, id);
        }
        throw new InvalidFeedNameException("Either feedSlug or id are not valid. Feed slug only accept words, feed id accepts words and hyphens");
    }
}
