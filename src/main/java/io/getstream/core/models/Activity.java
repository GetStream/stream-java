package io.getstream.core.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.getstream.core.models.serialization.DateDeserializer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Activity.Builder.class)
public class Activity {
  private final String id;
  private final String actor;
  private final String verb;
  private final String object;
  private final String foreignID;
  private final String target;
  // TODO: support Java 8 Date/Time types?
  private final Date time;
  private final String origin;
  private final List<FeedID> to;
  private final Double score;
  private final Map<String, Object> extra;

  private Activity(Builder builder) {
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
    extra = builder.extra;
  }

  public String getID() {
    return id;
  }

  public String getActor() {
    return actor;
  }

  public String getVerb() {
    return verb;
  }

  public String getObject() {
    return object;
  }

  @JsonProperty("foreign_id")
  public String getForeignID() {
    return foreignID;
  }

  public String getTarget() {
    return target;
  }

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
      lenient = OptBoolean.FALSE,
      timezone = "UTC")
  public Date getTime() {
    return time;
  }

  public String getOrigin() {
    return origin;
  }

  public List<FeedID> getTo() {
    return to;
  }

  public Double getScore() {
    return score;
  }

  @JsonAnyGetter
  public Map<String, Object> getExtra() {
    return extra;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Activity activity = (Activity) o;
    return Objects.equals(id, activity.id)
        && Objects.equals(actor, activity.actor)
        && Objects.equals(verb, activity.verb)
        && Objects.equals(object, activity.object)
        && Objects.equals(foreignID, activity.foreignID)
        && Objects.equals(target, activity.target)
        && Objects.equals(time, activity.time)
        && Objects.equals(origin, activity.origin)
        && Objects.equals(to, activity.to)
        && Objects.equals(score, activity.score)
        && Objects.equals(extra, activity.extra);
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
        .add("extra", this.extra)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String id;
    private String actor;
    private String verb;
    private String object;
    private String foreignID;
    private String target;
    private Date time;
    private String origin;
    private List<FeedID> to;
    private Double score;
    private Map<String, Object> extra;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder actor(String actor) {
      this.actor = actor;
      return this;
    }

    public Builder verb(String verb) {
      this.verb = verb;
      return this;
    }

    public Builder object(String object) {
      this.object = object;
      return this;
    }

    @JsonProperty("foreign_id")
    public Builder foreignID(String foreignID) {
      this.foreignID = foreignID;
      return this;
    }

    public Builder target(String target) {
      this.target = target;
      return this;
    }

    @JsonDeserialize(using = DateDeserializer.class)
    public Builder time(Date time) {
      this.time = time;
      return this;
    }

    public Builder origin(String origin) {
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
    public Builder fromActivity(Activity activity) {
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
      this.extra = activity.extra;
      return this;
    }

    @JsonIgnore
    public <T> Builder fromCustomActivity(T custom) {
      return fromActivity(convert(custom, Activity.class));
    }

    public Activity build() {
      checkNotNull(actor, "Activity 'actor' field required");
      checkNotNull(verb, "Activity 'verb' field required");
      checkNotNull(object, "Activity 'object' field required");

      return new Activity(this);
    }
  }
}
