package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

public final class Content {
    private final String foreignID;
    private final Map<String, Object> data = Maps.newHashMap();

    @JsonCreator
    public Content(@JsonProperty("foreign_id") String foreignID) {
        this.foreignID = checkNotNull(foreignID, "ID required");
    }

    public static <T> Content buildFrom(T data) {
        return convert(data, Content.class);
    }

    @JsonProperty("foreign_id")
    public String getForeignID() {
        return foreignID;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    @JsonAnySetter
    public <T> Content set(String key, T value) {
        checkArgument(!"foreignID".equals(key), "Key can't be named 'foreignID'");
        checkNotNull(key, "Key can't be null");
        checkNotNull(value, "Value can't be null");

        data.put(key, value);
        return this;
    }

    public <T> Content from(T data) {
        checkNotNull(data, "Can't extract data from null");

        Map<String, Object> map = convert(data, new TypeReference<Map<String, Object>>() {});
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public <T> T get(String key) {
        return (T) data.get(checkNotNull(key, "Key can't be null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content collectionData = (Content) o;
        return Objects.equals(foreignID, collectionData.foreignID) &&
                Objects.equals(data, collectionData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foreignID, data);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.foreignID)
                .add("data", this.data)
                .toString();
    }
}
