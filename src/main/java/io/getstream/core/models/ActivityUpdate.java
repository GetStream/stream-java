package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityUpdate {
    private final String id;
    private final String foreignID;
    private final Date time;
    private final Map<String, Object> set;
    private final List<String> unset;

    ActivityUpdate(Builder builder) {
        if (builder.id != null) {
            id = builder.id;
            foreignID = null;
            time = null;
        } else {
            id = null;
            foreignID = builder.foreignID;
            time = builder.time;
        }
        set = builder.set;
        unset = builder.unset;
    }

    public String getID() {
        return id;
    }

    @JsonProperty("foreign_id")
    public String getForeignID() {
        return foreignID;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", lenient = OptBoolean.FALSE, timezone = "UTC")
    public Date getTime() {
        return time;
    }

    public Map<String, Object> getSet() {
        return set;
    }

    public List<String> getUnset() {
        return unset;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String foreignID;
        private Date time;
        private Map<String, Object> set;
        private List<String> unset;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder foreignID(String foreignID) {
            this.foreignID = foreignID;
            return this;
        }

        public Builder time(Date time) {
            this.time = time;
            return this;
        }

        public Builder foreignIDTimePair(ForeignIDTimePair pair) {
            foreignID = pair.getForeignID();
            time = pair.getTime();
            return this;
        }

        public Builder set(Map<String, Object> set) {
            this.set = ImmutableMap.copyOf(set);
            return this;
        }

        public Builder set(Iterable<Map.Entry<String, Object>> set) {
            this.set = ImmutableMap.copyOf(set);
            return this;
        }

        public Builder unset(Iterable<String> unset) {
            this.unset = Lists.newArrayList(unset);
            return this;
        }

        public Builder unset(String... unset) {
            this.unset = Lists.newArrayList(unset);
            return this;
        }

        public ActivityUpdate build() {
            return new ActivityUpdate(this);
        }
    }
}
