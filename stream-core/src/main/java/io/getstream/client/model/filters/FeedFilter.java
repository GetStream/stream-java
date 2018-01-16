package io.getstream.client.model.filters;

import java.util.List;

/**
 * General filter to filter out activities/follower/following.
 */
public class FeedFilter {

    public final static String PARAM_LIMIT = "limit";
    public final static String PARAM_OFFSET = "offset";
    public final static String PARAM_ID_GREATER_THAN = "id_gt";
    public final static String PARAM_ID_GREATER_THAN_EQUALS = "id_gte";
    public final static String PARAM_ID_LOWER_THAN = "id_lt";
    public final static String PARAM_ID_LOWER_THAN_EQUALS = "id_lte";
    public final static String PARAM_FEED_IDS = "filter";
    public final static String PARAM_RANKING = "ranking";
    public final static String PARAM_SESSION = "session";

    private static final int DEFAULT_LIMIT = 25;

    private Integer limit = DEFAULT_LIMIT;
    private Integer offset = null;
    private String idGreaterThan = null;
    private String idGreaterThanEquals = null;
    private String idLowerThanEquals = null;
    private String idLowerThan = null;
    private String ranking = null;
    private String session = null;
    private List<String> feedIds = null;

    protected FeedFilter() {
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getIdGreaterThan() {
        return idGreaterThan;
    }

    public String getIdGreaterThanEquals() {
        return idGreaterThanEquals;
    }

    public String getIdLowerThanEquals() {
        return idLowerThanEquals;
    }

    public String getIdLowerThan() {
        return idLowerThan;
    }

    public List<String> getFeedIds() {
        return feedIds;
    }

    public String getRanking() {
        return ranking;
    }

    public String getSession() {
        return session;
    }

    /**
     * Builder to build up a {@link FeedFilter}.
     */
    public static class Builder {
        protected final FeedFilter feedFilter;

        public Builder() {
            feedFilter = new FeedFilter();
        }

        /**
         * Create a {@link FeedFilter}.
         *
         * @return A new filter
         */
        public FeedFilter build() {
            return feedFilter;
        }

        public Builder withLimit(int limit) {
            feedFilter.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            feedFilter.offset = offset;
            return this;
        }

        public Builder withIdGreaterThan(String idGreaterThan) {
            feedFilter.idGreaterThan = idGreaterThan;
            return this;
        }

        public Builder withIdGreaterThanEquals(String idGreaterThanEquals) {
            feedFilter.idGreaterThanEquals = idGreaterThanEquals;
            return this;
        }

        public Builder withIdLowerThanEquals(String idLowerThanEquals) {
            feedFilter.idLowerThanEquals = idLowerThanEquals;
            return this;
        }

        public Builder withIdLowerThan(String idLowerThan) {
            feedFilter.idLowerThan = idLowerThan;
            return this;
        }

        public Builder withRanking(String ranking) {
            feedFilter.ranking = ranking;
            return this;
        }

        public Builder withSession(String session) {
            feedFilter.session = session;
            return this;
        }

        public Builder withFeedIds(List<String> feeds) {
            feedFilter.feedIds = feeds;
            return this;
        }
    }
}
