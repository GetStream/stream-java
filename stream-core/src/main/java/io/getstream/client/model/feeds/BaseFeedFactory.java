package io.getstream.client.model.feeds;

import io.getstream.client.exception.InvalidFeedNameException;
import io.getstream.client.repo.StreamRepository;

import java.util.regex.Pattern;

/**
 * Factory class to build a new instance of a feed.
 */
public final class BaseFeedFactory implements FeedFactory {

    private final static Pattern FEED_SLUG_PATTERN = Pattern.compile(FEED_SLUG_ALLOWED_PATTERN);
    private final static Pattern FEED_ID_PATTERN = Pattern.compile(FEED_ID_ALLOWED_PATTERN);

    private final StreamRepository streamRepository;

    public BaseFeedFactory(final StreamRepository streamRepository) {
        this.streamRepository = streamRepository;
    }

    @Override
    public Feed createFeed(final String feedSlug, final String id) throws InvalidFeedNameException {
        if (FEED_SLUG_PATTERN.matcher(feedSlug).matches() && FEED_ID_PATTERN.matcher(id).matches()) {
            return new BaseFeed(streamRepository, feedSlug, id);
        }
        throw new InvalidFeedNameException("Either feedSlug or id are not valid. Feed slug only accept words, feed id accepts words and hyphens");
    }
}
