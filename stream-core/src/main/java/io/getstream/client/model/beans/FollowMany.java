package io.getstream.client.model.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Helper bean used to create a payload for the follow_many API call.
 */
public class FollowMany {

    @JsonSerialize(contentAs = FollowMany.Entry.class)
    private final List<Entry> entries;

    private FollowMany(final List<Entry> entries) {
        this.entries = entries;
    }

    @JsonValue
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Provide an easy way to build an immutable list of activity ids.
     */
    public static class Builder {
        private ImmutableList.Builder<Entry> followEntries = new ImmutableList.Builder<>();

        public Builder add(final String source, final String target) {
            this.followEntries.add(new Entry(source, target));
            return this;
        }

        public Builder addMany(final List<Entry> entries) {
            this.followEntries.addAll(entries);
            return this;
        }

        /**
         * Build an immutable list of marked activities.
         *
         * @return A marked activity
         */
        public FollowMany build() {
            return new FollowMany(followEntries.build());
        }
    }

    public static class Entry {

        private String source;
        private String target;

        @JsonCreator
        public Entry(@JsonProperty("source") final String source,
                     @JsonProperty("target") final String target) {
            this.source = source;
            this.target = target;
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
    }
}
