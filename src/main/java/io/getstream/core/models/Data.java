package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import io.getstream.core.models.serialization.DataDeserializer;

import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

@JsonDeserialize(using = DataDeserializer.class)
public class Data {
    private final String id;
    private final Map<String, Object> data = Maps.newHashMap();

    public Data(String id) {
        this.id = checkNotNull(id, "ID required");
    }

    public static <T> Data buildFrom(T data) {
        return convert(data, Data.class);
    }

    public String getID() {
        return id;
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public <T> Data set(String key, T value) {
        checkArgument(!"id".equals(key), "Key can't be named 'id'");
        checkNotNull(key, "Key can't be null");
        checkNotNull(value, "Value can't be null");

        data.put(key, value);
        return this;
    }

    public <T> Data from(T data) {
        return from(convert(data, new TypeReference<Map<String, Object>>() {}));
    }

    public Data from(Map<String, Object> map) {
        checkNotNull(data, "Can't extract data from null");

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
        Data data = (Data) o;
        return Objects.equals(id, data.id) &&
                Objects.equals(data, data.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", this.id)
                .add("data", this.data)
                .toString();
    }
}
