package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = Engagement.Builder.class)
public class Engagement {
    private final String feedID;
    private final UserData userData;
    private final String label;
    private final Content content;
    private final Integer boost;
    private final Integer position;
    private final String location;
    private final List<Feature> features;
    private final Date trackedAt;

    private Engagement(Builder builder) {
        label = builder.label;
        content = builder.content;
        boost = builder.boost;
        position = builder.position;
        feedID = builder.feedID;
        location = builder.location;
        userData = builder.userData;
        features = builder.features;
        trackedAt = builder.trackedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getLabel() {
        return label;
    }

    public Content getContent() {
        return content;
    }

    public int getBoost() {
        return boost;
    }

    public int getPosition() {
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
        Engagement that = (Engagement) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(content, that.content) &&
                Objects.equals(boost, that.boost) &&
                Objects.equals(position, that.position) &&
                Objects.equals(feedID, that.feedID) &&
                Objects.equals(location, that.location) &&
                Objects.equals(userData, that.userData) &&
                Objects.equals(features, that.features) &&
                Objects.equals(trackedAt, that.trackedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, content, boost, position, feedID, location, userData, features, trackedAt);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("label", this.label)
                .add("content", this.content)
                .add("boost", this.boost)
                .add("position", this.position)
                .add("feedID", this.feedID)
                .add("location", this.location)
                .add("userData", this.userData)
                .add("features", this.features)
                .add("trackedAt", this.trackedAt)
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String label;
        private Content content;
        private Integer boost;
        private Integer position;
        private String feedID;
        private String location;
        private UserData userData;
        private List features;
        private Date trackedAt;

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder content(Content content) {
            this.content = content;
            return this;
        }

        public Builder boost(int boost) {
            this.boost = boost;
            return this;
        }

        public Builder position(int position) {
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
        public Builder fromEngagement(Engagement engagement) {
            label = engagement.label;
            content = engagement.content;
            boost = engagement.boost;
            position = engagement.position;
            feedID = engagement.feedID;
            location = engagement.location;
            userData = engagement.userData;
            features = engagement.features;
            trackedAt = engagement.trackedAt;
            return this;
        }

        @JsonIgnore
        public <T> Builder fromCustomEngagement(T custom) {
            return fromEngagement(convert(custom, Engagement.class));
        }

        public Engagement build() {
            checkNotNull(feedID, "Engagement 'feedID' field required");
            checkNotNull(userData, "Engagement 'userData' field required");
            checkNotNull(label, "Engagement 'label' field required");
            checkNotNull(content, "Engagement 'content' field required");

            return new Engagement(this);
        }
    }
}
