package io.getstream.core.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = EnrichedActivity.Builder.class)
public class EnrichedActivity {
    private final String id;
    private final Data actor;
    private final Data verb;
    private final Data object;
    private final String foreignID;
    private final Data target;
    //TODO: support Java 8 Date/Time types?
    private final Date time;
    private final Data origin;
    private final List<FeedID> to;
    private final Double score;
    private final Map<String, Number> reactionCounts;
    private final Map<String, List<Reaction>> ownReactions;
    private final Map<String, List<Reaction>> latestReactions;
    private final Map<String, Object> extra;

    private EnrichedActivity(Builder builder) {
        id = builder.id;
        actor = builder.actor;
        verb = builder.verb;
        object = builder.object;
        foreignID = builder.foreignID;
        target = builder.target;
        time = builder.time;
        origin = builder.origin;
        to = builder.to;
        score = builder.score;
        reactionCounts = builder.reactionCounts;
        ownReactions = builder.ownReactions;
        latestReactions = builder.latestReactions;
        extra = builder.extra;
    }

    public String getID() {
        return id;
    }

    public Data getActor() {
        return actor;
    }

    public Data getVerb() {
        return verb;
    }

    public Data getObject() {
        return object;
    }

    @JsonProperty("foreign_id")
    public String getForeignID() {
        return foreignID;
    }

    public Data getTarget() {
        return target;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.S", timezone = "UTC")
    public Date getTime() {
        return time;
    }

    public Data getOrigin() {
        return origin;
    }

    public List<FeedID> getTo() {
        return to;
    }

    public Double getScore() {
        return score;
    }

    public Map<String, Number> getReactionCounts() {
        return reactionCounts;
    }

    public Map<String, List<Reaction>> getOwnReactions() {
        return ownReactions;
    }

    public Map<String, List<Reaction>> getLatestReactions() {
        return latestReactions;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getExtra() {
        return extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedActivity activity = (EnrichedActivity) o;
        return Objects.equals(id, activity.id) &&
                Objects.equals(actor, activity.actor) &&
                Objects.equals(verb, activity.verb) &&
                Objects.equals(object, activity.object) &&
                Objects.equals(foreignID, activity.foreignID) &&
                Objects.equals(target, activity.target) &&
                Objects.equals(time, activity.time) &&
                Objects.equals(origin, activity.origin) &&
                Objects.equals(to, activity.to) &&
                Objects.equals(score, activity.score) &&
                Objects.equals(extra, activity.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actor, verb, object, foreignID, target, time, origin, to, score, extra);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("actor", this.actor)
                .add("verb", this.verb)
                .add("object", this.object)
                .add("foreignID", this.foreignID)
                .add("target", this.target)
                .add("time", this.time)
                .add("origin", this.origin)
                .add("to", this.to)
                .add("score", this.score)
                .add("ownReactions", this.ownReactions)
                .add("latestReactions", this.latestReactions)
                .add("reactionCounts", this.reactionCounts)
                .add("extra", this.extra)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String id;
        private Data actor;
        private Data verb;
        private Data object;
        private String foreignID;
        private Data target;
        private Date time;
        private Data origin;
        private List<FeedID> to;
        private Double score;
        private Map<String, Number> reactionCounts;
        private Map<String, List<Reaction>> ownReactions;
        private Map<String, List<Reaction>> latestReactions;
        private Map<String, Object> extra;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        @JsonIgnore
        public Builder actor(String actor) {
            this.actor = new Data(actor);
            return this;
        }

        @JsonProperty("actor")
        public Builder actor(Data actor) {
            this.actor = actor;
            return this;
        }

        @JsonIgnore
        public Builder verb(String verb) {
            this.verb = new Data(verb);
            return this;
        }

        @JsonProperty("verb")
        public Builder verb(Data verb) {
            this.verb = verb;
            return this;
        }

        @JsonIgnore
        public Builder object(String object) {
            this.object = new Data(object);
            return this;
        }

        @JsonProperty("object")
        public Builder object(Data object) {
            this.object = object;
            return this;
        }

        @JsonProperty("foreign_id")
        public Builder foreignID(String foreignID) {
            this.foreignID = foreignID;
            return this;
        }

        @JsonIgnore
        public Builder target(String target) {
            this.target = new Data(target);
            return this;
        }

        @JsonProperty("target")
        public Builder target(Data target) {
            this.target = target;
            return this;
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.S", timezone = "UTC")
        public Builder time(Date time) {
            this.time = time;
            return this;
        }

        @JsonIgnore
        public Builder origin(String origin) {
            this.origin = new Data(origin);
            return this;
        }

        @JsonProperty("origin")
        public Builder origin(Data origin) {
            this.origin = origin;
            return this;
        }

        @JsonProperty("to")
        public Builder to(List<FeedID> to) {
            this.to = to;
            return this;
        }

        @JsonIgnore
        public Builder to(Iterable<FeedID> to) {
            this.to = Lists.newArrayList(to);
            return this;
        }

        @JsonIgnore
        public Builder to(FeedID... to) {
            this.to = Lists.newArrayList(to);
            return this;
        }

        public Builder score(double score) {
            this.score = score;
            return this;
        }

        @JsonProperty("own_reactions")
        public Builder ownReactions(Map<String, List<Reaction>> ownReactions) {
            this.ownReactions = ownReactions;
            return this;
        }

        @JsonProperty("latest_reactions")
        public Builder latestReactions(Map<String, List<Reaction>> latestReactions) {
            this.latestReactions = latestReactions;
            return this;
        }

        @JsonProperty("reaction_counts")
        public Builder reactionCounts(Map<String, Number> reactionCounts) {
            this.reactionCounts = reactionCounts;
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
            if (!extra.isEmpty()) {
                this.extra = extra;
            }
            return this;
        }

        @JsonIgnore
        public Builder fromEnrichedActivity(EnrichedActivity activity) {
            this.id = activity.id;
            this.actor = activity.actor;
            this.verb = activity.verb;
            this.object = activity.object;
            this.foreignID = activity.foreignID;
            this.target = activity.target;
            this.time = activity.time;
            this.origin = activity.origin;
            this.to = activity.to;
            this.score = activity.score;
            this.ownReactions = activity.ownReactions;
            this.latestReactions = activity.latestReactions;
            this.reactionCounts = activity.reactionCounts;
            this.extra = activity.extra;
            return this;
        }

        @JsonIgnore
        public <T> Builder fromCustomEnrichedActivity(T custom) {
            return fromEnrichedActivity(convert(custom, EnrichedActivity.class));
        }

        public EnrichedActivity build() {
            checkNotNull(actor, "EnrichedActivity 'actor' field required");
            checkNotNull(verb, "EnrichedActivity 'verb' field required");
            checkNotNull(object, "EnrichedActivity 'object' field required");

            return new EnrichedActivity(this);
        }
    }
}
