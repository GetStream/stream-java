package io.getstream.core.models;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Impression.Builder.class)
public class Impression {
  private final String feedID;
  private final UserData userData;
  private final List<Content> contentList;
  private final String position;
  private final String location;
  private final List<Feature> features;
  private final Date trackedAt;

  private Impression(Builder builder) {
    position = builder.position;
    feedID = builder.feedID;
    location = builder.location;
    userData = builder.userData;
    contentList = builder.contentList;
    features = builder.features;
    trackedAt = builder.trackedAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getPosition() {
    return position;
  }

  @JsonProperty("feed_id")
  public String getFeedID() {
    return feedID;
  }

  public String getLocation() {
    return location;
  }

  @JsonProperty("user_data")
  public UserData getUserData() {
    return userData;
  }

  @JsonProperty("content_list")
  public List<Content> getContentList() {
    return contentList;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  @JsonProperty("tracked_at")
  public Date getTrackedAt() {
    return trackedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Impression that = (Impression) o;
    return Objects.equals(position, that.position)
        && Objects.equals(feedID, that.feedID)
        && Objects.equals(location, that.location)
        && Objects.equals(userData, that.userData)
        && Objects.equals(contentList, that.contentList)
        && Objects.equals(features, that.features)
        && Objects.equals(trackedAt, that.trackedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, feedID, location, userData, contentList, features, trackedAt);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("position", this.position)
        .add("feedID", this.feedID)
        .add("location", this.location)
        .add("userData", this.userData)
        .add("contentList", this.contentList)
        .add("features", this.features)
        .add("trackedAt", this.trackedAt)
        .toString();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static final class Builder {
    private String position;
    private String feedID;
    private String location;
    private UserData userData;
    private List<Content> contentList;
    private List features;
    private Date trackedAt;

    public Builder position(String position) {
      this.position = position;
      return this;
    }

    @JsonProperty("feed_id")
    public Builder feedID(String feedID) {
      this.feedID = feedID;
      return this;
    }

    public Builder location(String location) {
      this.location = location;
      return this;
    }

    @JsonProperty("user_data")
    public Builder userData(UserData userData) {
      this.userData = userData;
      return this;
    }

    @JsonProperty("content_list")
    public Builder contentList(List<Content> contentList) {
      this.contentList = contentList;
      return this;
    }

    @JsonIgnore
    public Builder contentList(Iterable<Content> contentList) {
      this.contentList = Lists.newArrayList(contentList);
      return this;
    }

    @JsonIgnore
    public Builder contentList(Content... contentList) {
      this.contentList = Lists.newArrayList(contentList);
      return this;
    }

    public Builder features(List features) {
      this.features = features;
      return this;
    }

    @JsonProperty("tracked_at")
    public Builder trackedAt(Date trackedAt) {
      this.trackedAt = trackedAt;
      return this;
    }

    @JsonIgnore
    public Builder fromImpression(Impression impression) {
      position = impression.position;
      feedID = impression.feedID;
      location = impression.location;
      userData = impression.userData;
      contentList = impression.contentList;
      features = impression.features;
      trackedAt = impression.trackedAt;
      return this;
    }

    @JsonIgnore
    public <T> Builder fromCustomImpression(T custom) {
      return fromImpression(convert(custom, Impression.class));
    }

    public Impression build() {
      checkNotNull(feedID, "Impression 'feedID' field required");
      checkNotNull(userData, "Impression 'userData' field required");

      return new Impression(this);
    }
  }
}
