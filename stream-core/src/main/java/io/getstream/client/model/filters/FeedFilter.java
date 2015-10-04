/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
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

    private static final int DEFAULT_LIMIT = 25;

    private Integer limit = DEFAULT_LIMIT;
    private Integer offset = null;
    private String idGreaterThan = null;
    private String idGreaterThanEquals = null;
    private String idLowerThanEquals = null;
    private String idLowerThan = null;
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

    }
}
