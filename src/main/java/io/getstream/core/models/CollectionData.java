package io.getstream.core.models;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Serialization.convert;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;

public final class CollectionData {
  private final String id;
  private final String collection;
  private final Map<String, Object> data;

  @JsonCreator
  public CollectionData(
      @JsonProperty("collection") String collection,
      @JsonProperty("id") String id,
      @JsonProperty("data") Map<String, Object> data) {
    this.collection = collection;
    this.data = firstNonNull(data, Maps.newHashMap());
    this.id = checkNotNull(id, "ID required");
  }

  public CollectionData() {
    this(null, "", null);
  }

  public CollectionData(String id) {
    this(null, id, null);
  }

  public static <T> CollectionData buildFrom(T data) {
    return convert(data, CollectionData.class);
  }

  public String getID() {
    return id;
  }

  @JsonIgnore
  public String getCollection() {
    return collection;
  }

  @JsonAnyGetter
  public Map<String, Object> getData() {
    return data;
  }

  @JsonAnySetter
  public <T> CollectionData set(String key, T value) {
    checkArgument(!"id".equals(key), "Key can't be named 'id'");
    checkNotNull(key, "Key can't be null");

    data.put(key, value);
    return this;
  }

  public <T> CollectionData from(T data) {
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
    CollectionData collectionData = (CollectionData) o;
    return Objects.equals(id, collectionData.id) && Objects.equals(data, collectionData.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, data);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", this.id).add("data", this.data).toString();
  }
}
