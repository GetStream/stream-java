package io.getstream.core.models;

import static io.getstream.core.utils.Serialization.convert;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Reaction.Builder.class)
public class Reaction {
  private final String id;
  private final Integer appID;
  private final String kind;
  private final String userID;
  private final String activityID;
  private final String parent;
  private final Map<String, List<Reaction>> ownChildren;
  private final Map<String, List<Reaction>> latestChildren;
  private final Map<String, Number> childrenCounts;
  private final Data userData;
  private final Map<String, Object> activityData;
  private final Map<String, Object> extra;
  private String moderationTemplate;
  private Map<String, Object> moderation;

  private Reaction(Builder builder) {
    id = builder.id;
    appID = builder.appID;
    kind = builder.kind;
    userID = builder.userID;
    activityID = builder.activityID;
    parent = builder.parent;
    ownChildren = builder.ownChildren;
    latestChildren = builder.latestChildren;
    childrenCounts = builder.childrenCounts;
    userData = builder.userData;
    activityData = builder.activityData;
    extra = builder.extra;
    moderationTemplate = builder.moderationTemplate;
    moderation = builder.moderation;
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

  public String getParent() {
    return parent;
  }

  @JsonIgnore
  public Map<String, List<Reaction>> getOwnChildren() {
    return ownChildren;
  }

  @JsonIgnore
  public Map<String, List<Reaction>> getLatestChildren() {
    return latestChildren;
  }

  @JsonIgnore
  public Map<String, Number> getChildrenCounts() {
    return childrenCounts;
  }

  @JsonIgnore
  public Data getUserData() {
    return userData;
  }

  @JsonIgnore
  public Map<String, Object> getActivityData() {
    return activityData;
  }

  @JsonAnyGetter
  public Map<String, Object> getExtra() {
    return extra;
  }

  @JsonProperty("moderation_template")
  public String getModerationTemplate() {
    return moderationTemplate;
  }

  public void setModerationTemplate(String moderationTemplate) {
    this.moderationTemplate = moderationTemplate;
  }

  @JsonProperty("moderation")
  public Map<String, Object> getModeration() {
    return moderation;
  }

  public ModerationResponse getModerationResponse() throws Exception {
    String key = "response";
    if (moderation != null && moderation.containsKey(key)) {
      return convert(moderation.get(key), ModerationResponse.class);
    } else {
      throw new Exception("Key '" + key + "' not found in moderation map.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reaction reaction = (Reaction) o;
    return Objects.equals(id, reaction.id)
        && Objects.equals(appID, reaction.appID)
        && Objects.equals(kind, reaction.kind)
        && Objects.equals(userID, reaction.userID)
        && Objects.equals(activityID, reaction.activityID)
        && Objects.equals(parent, reaction.parent)
        && Objects.equals(ownChildren, reaction.ownChildren)
        && Objects.equals(latestChildren, reaction.latestChildren)
        && Objects.equals(childrenCounts, reaction.childrenCounts)
        && Objects.equals(userData, reaction.userData)
        && Objects.equals(activityData, reaction.activityData)
        && Objects.equals(extra, reaction.extra)
        && Objects.equals(moderationTemplate, reaction.moderationTemplate)
        && Objects.equals(moderation, reaction.moderation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        appID,
        kind,
        userID,
        activityID,
        parent,
        ownChildren,
        latestChildren,
        childrenCounts,
        userData,
        activityData,
        extra,
        moderationTemplate,
        moderation);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", this.id)
        .add("appID", this.appID)
        .add("kind", this.kind)
        .add("userID", this.userID)
        .add("activityID", this.activityID)
        .add("parent", this.parent)
        .add("ownChildren", this.ownChildren)
        .add("latestChildren", this.latestChildren)
        .add("childrenCounts", this.childrenCounts)
        .add("userData", this.userData)
        .add("activityData", this.activityData)
        .add("extra", this.extra)
        .add("moderationTemplate", this.moderationTemplate)
        .add("moderation", this.moderation)
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
    private String parent;
    private Map<String, List<Reaction>> ownChildren;
    private Map<String, List<Reaction>> latestChildren;
    private Map<String, Number> childrenCounts;
    private Data userData;
    private Map<String, Object> activityData;
    private Map<String, Object> extra;
    private String moderationTemplate;
    private Map<String, Object> moderation;

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

    @JsonProperty("user_id")
    public Builder userID(String userID) {
      this.userID = userID;
      return this;
    }

    @JsonProperty("activity_id")
    public Builder activityID(String activityID) {
      this.activityID = activityID;
      return this;
    }

    public Builder parent(String parent) {
      this.parent = parent;
      return this;
    }

    @JsonProperty("own_children")
    public Builder ownChildren(Map<String, List<Reaction>> ownChildren) {
      this.ownChildren = ownChildren;
      return this;
    }

    @JsonProperty("latest_children")
    public Builder latestChildren(Map<String, List<Reaction>> latestChildren) {
      this.latestChildren = latestChildren;
      return this;
    }

    @JsonProperty("children_counts")
    public Builder childrenCounts(Map<String, Number> childrenCounts) {
      this.childrenCounts = childrenCounts;
      return this;
    }

    @JsonProperty("user")
    public Builder userData(Data userData) {
      this.userData = userData;
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

    @JsonProperty("data")
    public Builder activityData(Map<String, Object> activityData) {
      this.activityData = activityData;
      return this;
    }

    @JsonProperty("moderation_template")
    public Builder moderationTemplate(String moderationTemplate) {
      this.moderationTemplate = moderationTemplate;
      return this;
    }

    @JsonProperty("moderation")
    public Builder moderation(Map<String, Object> moderation) {
      this.moderation = moderation;
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
      this.parent = reaction.parent;
      this.ownChildren = reaction.ownChildren;
      this.latestChildren = reaction.latestChildren;
      this.childrenCounts = reaction.childrenCounts;
      this.userData = reaction.userData;
      this.activityData = reaction.activityData;
      this.extra = reaction.extra;
      this.moderationTemplate = reaction.moderationTemplate;
      this.moderation = reaction.moderation;
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
