package io.getstream.client.model.beans;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class MarkedActivity {

    private final List<String> activityIds;

    private MarkedActivity(final List<String> ids) {
        this.activityIds = ids;
    }

    public List<String> getActivities() {
        return this.activityIds;
    }

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

        public MarkedActivity build() {
            return new MarkedActivity(markedActivity.build());
        }
    }
}
