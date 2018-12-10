package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class FollowRelation {
    private final String source;
    private final String target;

    @JsonCreator
    public FollowRelation(@JsonProperty("feed_id") String source, @JsonProperty("target_id") String target) {
        checkNotNull(source, "FollowRelation 'source' field required");
        checkNotNull(target, "FollowRelation 'target' field required");

        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowRelation that = (FollowRelation) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", this.source)
                .add("target", this.target)
                .toString();
    }
}
