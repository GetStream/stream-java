package io.getstream.client.model.filters;

import com.google.common.base.Joiner;
import io.getstream.client.utils.UriBuilder;

import java.util.List;

public class FeedFilter {

	private final static String PARAM_LIMIT = "limit";
	private final static String PARAM_OFFSET = "offset";
	private final static String PARAM_ID_GREATER_THAN = "id_gt";
	private final static String PARAM_ID_GREATER_THAN_EQUALS = "id_gte";
	private final static String PARAM_ID_LOWER_THAN = "id_lt";
	private final static String PARAM_ID_LOWER_THAN_EQUALS = "id_lte";
	private final static String PARAM_FEED_IDS = "filter";

	private static final int DEFAULT_LIMIT = 25;

	private Integer limit = DEFAULT_LIMIT;
	private Integer offset = null;
	private Integer idGreaterThan = null;
	private Integer idGreaterThanEquals = null;
	private Integer idLowerThanEquals = null;
	private Integer idLowerThan = null;
	private List<String> feedIds = null;

	private FeedFilter() {
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public int getIdGreaterThan() {
		return idGreaterThan;
	}

	public int getIdGreaterThanEquals() {
		return idGreaterThanEquals;
	}

	public int getIdLowerThanEquals() {
		return idLowerThanEquals;
	}

	public int getIdLowerThan() {
		return idLowerThan;
	}

	public List<String> getFeedIds() {
		return feedIds;
	}

	public UriBuilder apply(UriBuilder uriBuilder) {
		if (null != this.limit) {
			uriBuilder.queryParam(PARAM_LIMIT, this.limit);
		}
		if (null != this.offset) {
			uriBuilder.queryParam(PARAM_OFFSET, this.offset);
		}
		if (null != this.idGreaterThan) {
			uriBuilder.queryParam(PARAM_ID_GREATER_THAN, this.idGreaterThan);
		}
		if (null != this.idGreaterThanEquals) {
			uriBuilder.queryParam(PARAM_ID_GREATER_THAN_EQUALS, this.idGreaterThanEquals);
		}
		if (null != this.idLowerThan) {
			uriBuilder.queryParam(PARAM_ID_LOWER_THAN, this.idLowerThan);
		}
		if (null != this.idLowerThanEquals) {
			uriBuilder.queryParam(PARAM_ID_LOWER_THAN_EQUALS, this.idLowerThanEquals);
		}
		if (null != this.feedIds) {
			uriBuilder.queryParam(PARAM_FEED_IDS, Joiner.on(",").join(this.feedIds));
		}
	 	return uriBuilder;
	}

	public static class Builder {
		private final FeedFilter feedFilter;

		public Builder() {
			feedFilter = new FeedFilter();
		}

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

		public Builder withIdGreaterThan(int idGreaterThan) {
			feedFilter.idGreaterThan = idGreaterThan;
			return this;
		}

		public Builder withIdGreaterThanEquals(int idGreaterThanEquals) {
			feedFilter.idGreaterThanEquals = idGreaterThanEquals;
			return this;
		}

		public Builder withIdLowerThanEquals(int idLowerThanEquals) {
			feedFilter.idLowerThanEquals = idLowerThanEquals;
			return this;
		}

		public Builder withIdLowerThan(int idLowerThan) {
			feedFilter.idLowerThan = idLowerThan;
			return this;
		}

		public Builder withFeedIds(List<String> feedIds) {
			feedFilter.feedIds = feedIds;
			return this;
		}
	}
}
