package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Helper bean used to perform bulk unfollow.
 */
public class UnfollowMany {

    @JsonSerialize(contentAs = UnfollowMany.Entry.class)
    private final List<Entry> entries;

    private UnfollowMany(final List<Entry> entries) {
        this.entries = entries;
    }

    @JsonValue
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Provide an easy way to build an immutable list of unfollow.
     */
    public static class Builder {
        private ImmutableList.Builder<Entry> followEntries = new ImmutableList.Builder<>();

        /**
         * Add a new unfollow source/target pair. Keep history is set to false.
         * @param source Source feed
         * @param target Target feed
         * @return This builder.
         */
        public Builder add(final String source, final String target) {
            this.followEntries.add(new Entry(source, target, false));
            return this;
        }

        /**
         * Add a new unfollow source/target pair.
         * @param source Source feed
         * @param target Target feed
         * @param keepHistory Whether the history must be preserved.
         * @return This builder.
         */
        public Builder add(final String source, final String target, final boolean keepHistory) {
            this.followEntries.add(new Entry(source, target, keepHistory));
            return this;
        }

        public Builder addMany(final List<Entry> entries) {
            this.followEntries.addAll(entries);
            return this;
        }

        /**
         * Build an immutable list of unfollow.
         *
         * @return A marked activity
         */
        public UnfollowMany build() {
            return new UnfollowMany(followEntries.build());
        }
    }

    public static class Entry {

        private String source;
        private String target;
        private boolean keepHistory;

        @JsonCreator
        public Entry(@JsonProperty("source") final String source,
                     @JsonProperty("target") final String target,
                     @JsonProperty("keep_history") final boolean keepHistory) {
            this.source = source;
            this.target = target;
            this.keepHistory = keepHistory;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public boolean getKeepHistory() {
            return keepHistory;
        }

        public void setKeepHistory(boolean keepHistory) {
            this.keepHistory = keepHistory;
        }
    }
}
