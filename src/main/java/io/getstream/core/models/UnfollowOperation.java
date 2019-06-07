package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.getstream.core.KeepHistory;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnfollowOperation {
    private final String source;
    private final String target;
    private final KeepHistory keepHistory;

    @JsonCreator
    public UnfollowOperation(@JsonProperty("feed_id") String source, @JsonProperty("target_id") String target, @JsonProperty("keep_history") KeepHistory keepHistory) {
        checkNotNull(source, "UnfollowOperation 'source' field required");
        checkNotNull(target, "UnfollowOperation 'target' field required");
        checkNotNull(keepHistory, "UnfollowOperation 'keep history' field required");

        this.source = source;
        this.target = target;
        this.keepHistory = keepHistory;
    }

    public UnfollowOperation(FollowRelation follow, KeepHistory keepHistory) {
        checkNotNull(follow, "UnfollowOperation 'follow' field required");
        checkNotNull(keepHistory, "UnfollowOperation 'keep history' field required");

        this.source = follow.getSource();
        this.target = follow.getTarget();
        this.keepHistory = keepHistory;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    public KeepHistory getKeepHistory() {
        return this.keepHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnfollowOperation that = (UnfollowOperation) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                Objects.equals(keepHistory, that.keepHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, keepHistory);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", this.source)
                .add("target", this.target)
                .add("keep_history", this.keepHistory)
                .toString();
    }
}
