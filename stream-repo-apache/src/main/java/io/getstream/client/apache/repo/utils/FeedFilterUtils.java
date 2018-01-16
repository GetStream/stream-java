package io.getstream.client.apache.repo.utils;

import com.google.common.base.Joiner;
import io.getstream.client.model.filters.FeedFilter;

public class FeedFilterUtils {

    private FeedFilterUtils() {
        throw new AssertionError();
    }

    /**
     * Apply the filter on the given URI.
     *
     * @param uriBuilder Add attributes to the given {@link UriBuilder}.
     * @param filter Instance of a filter
     * @return A {@link UriBuilder}
     */
    public static UriBuilder apply(final UriBuilder uriBuilder, final FeedFilter filter) {
        if (null == filter) {
            return uriBuilder;
        }
        if (null != filter.getLimit()) {
            uriBuilder.queryParam(FeedFilter.PARAM_LIMIT, filter.getLimit());
        }
        if (null != filter.getOffset()) {
            uriBuilder.queryParam(FeedFilter.PARAM_OFFSET, filter.getOffset());
        }
        if (null != filter.getIdGreaterThan()) {
            uriBuilder.queryParam(FeedFilter.PARAM_ID_GREATER_THAN, filter.getIdGreaterThan());
        }
        if (null != filter.getIdGreaterThanEquals()) {
            uriBuilder.queryParam(FeedFilter.PARAM_ID_GREATER_THAN_EQUALS, filter.getIdGreaterThanEquals());
        }
        if (null != filter.getIdLowerThan()) {
            uriBuilder.queryParam(FeedFilter.PARAM_ID_LOWER_THAN, filter.getIdLowerThan());
        }
        if (null != filter.getIdLowerThanEquals()) {
            uriBuilder.queryParam(FeedFilter.PARAM_ID_LOWER_THAN_EQUALS, filter.getIdLowerThanEquals());
        }
        if (null != filter.getFeedIds()) {
            uriBuilder.queryParam(FeedFilter.PARAM_FEED_IDS, Joiner.on(",").join(filter.getFeedIds()));
        }
        if (null != filter.getRanking()) {
            uriBuilder.queryParam(FeedFilter.PARAM_RANKING, filter.getRanking());
        }
        if (null != filter.getSession()) {
            uriBuilder.queryParam(FeedFilter.PARAM_SESSION, filter.getSession());
        }
        return uriBuilder;
    }
}
