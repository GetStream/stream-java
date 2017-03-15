package io.getstream.client.model.beans;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import io.getstream.client.model.activities.BaseActivity;

import java.util.List;

/**
 * Allow a list of {@link BaseActivity} to be marked.
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

    public boolean hasActivities() {
        if (activityIds == null) {
            return false;
        }
        return !activityIds.isEmpty();
    }

    public String joinActivities() {
        return Joiner.on(",").join(this.getActivities());
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
         *
         * @return A marked activity
         */
        public MarkedActivity build() {
            return new MarkedActivity(markedActivity.build());
        }
    }
}
