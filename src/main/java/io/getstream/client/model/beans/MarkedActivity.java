package io.getstream.client.model.beans;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Allow a list of {@link io.getstream.client.model.activities.BaseActivity} to be marked.
 * The purpose of the mark is given by the context in which it used for.
 */
public class MarkedActivity {

    private final List<String> activityIds;

    private MarkedActivity(final List<String> ids) {
        this.activityIds = ids;
    }

    public List<String> getActivities() {
        return this.activityIds;
    }

	/**
	 * Provide an easy way to build an immutable list of activity ids.
	 */
    public static class Builder {
        private ImmutableList.Builder<String> markedActivity = new ImmutableList.Builder<>();

        public Builder withActivityId(final String id) {
            this.markedActivity.add(id);
            return this;
        }

        public Builder withActivityIds(final List<String> ids) {
            this.markedActivity.addAll(ids);
            return this;
        }

		/**
		 * Build an immutable list of marked activities.
		 * @return
		 */
        public MarkedActivity build() {
            return new MarkedActivity(markedActivity.build());
        }
    }
}
