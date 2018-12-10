package io.getstream.core.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

import static io.getstream.core.utils.Serialization.convert;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Reaction.Builder.class)
public class Reaction {
    private final String id;
    private final Integer appID;
    private final String kind;
    private final String userID;
    private final String activityID;
    private final Map<String, Object> extra;

    private Reaction(Builder builder) {
        id = builder.id;
        appID = builder.appID;
        kind = builder.kind;
        userID = builder.userID;
        activityID = builder.activityID;
        extra = builder.extra;
    }

    public String getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    @JsonProperty("user_id")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("activity_id")
    public String getActivityID() {
        return activityID;
    }

    @JsonAnyGetter
    public Map<String, Object> getExtra() {
        return  extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return Objects.equals(id, reaction.id) &&
                Objects.equals(appID, reaction.appID) &&
                Objects.equals(kind, reaction.kind) &&
                Objects.equals(userID, reaction.userID) &&
                Objects.equals(activityID, reaction.activityID) &&
                Objects.equals(extra, reaction.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appID, kind, userID, activityID, extra);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("appID", this.appID)
                .add("kind", this.kind)
                .add("userID", this.userID)
                .add("activityID", this.activityID)
                .add("extra", this.extra)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String id;
        private Integer appID;
        private String kind;
        private String userID;
        private String activityID;
        private Map<String, Object> extra;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder appID(int appID) {
            this.appID = appID;
            return this;
        }

        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder userID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder activityID(String activityID) {
            this.activityID = activityID;
            return this;
        }

        @JsonAnySetter
        public Builder extraField(String key, Object value) {
            if (extra == null) {
                extra = Maps.newHashMap();
            }
            extra.put(key, value);
            return this;
        }

        @JsonIgnore
        public Builder extra(Map<String, Object> extra) {
            this.extra = extra;
            return this;
        }

        @JsonIgnore
        public Builder fromReaction(Reaction reaction) {
            this.id = reaction.id;
            this.appID = reaction.appID;
            this.kind = reaction.kind;
            this.userID = reaction.userID;
            this.activityID = reaction.activityID;
            this.extra = reaction.extra;
            return this;
        }

        @JsonIgnore
        public <T> Builder fromCustomReaction(T custom) {
            return fromReaction(convert(custom, Reaction.class));
        }

        public Reaction build() {
            return new Reaction(this);
        }
    }
}
