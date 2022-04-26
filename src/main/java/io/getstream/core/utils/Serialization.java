package io.getstream.core.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.collect.Range;
import io.getstream.core.exceptions.StreamAPIException;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

public final class Serialization {
  private static ObjectMapper mapper = new ObjectMapper();

  public ObjectMapper getObjectMapper() {
    return mapper;
  }

  public void setObjectMapper(ObjectMapper mapper) {
    this.mapper = checkNotNull(mapper, "Missing object mapper");
  }

  private Serialization() {
    /* nothing to see here */
  }

  public static <T> byte[] toJSON(T obj) throws JsonProcessingException {
    return mapper.writeValueAsBytes(obj);
  }

  public static <T> T fromJSON(InputStream json, Class<T> type) throws IOException {
    return mapper.readValue(json, type);
  }

  public static <T> T fromJSON(InputStream json, TypeReference<T> type) throws IOException {
    return mapper.readValue(json, type);
  }

  public static <T> T fromJSON(InputStream json, String wrapper, JavaType type) throws IOException {
    JsonNode tree = mapper.readTree(json);
    for (String path : wrapper.split("\\.")) {
      tree = tree.findPath(path);
    }

    return mapper.readValue(mapper.treeAsTokens(tree), type);
  }

  public static <T, U> T convert(U obj, Class<T> type) {
    return mapper.convertValue(obj, type);
  }

  public static <T, U> T convert(U obj, TypeReference<T> type) {
    return mapper.convertValue(obj, type);
  }

  // TODO: move this to a more specialised utility class?
  private static final Range<Integer> normalResponseCodes = Range.closed(200, 299);

  // TODO: catch deserialization errors and give a nice suggestion about wrong feed slug type
  public static <T> List<T> deserializeContainer(Response response, Class<T> type)
      throws IOException, StreamException {
    return deserializeContainer(response, "results", type);
  }

  public static <T> T deserializeContainer(
      Response response, Class<?> wrapperType, Class<?>... types)
      throws IOException, StreamException {
    final JavaType wrappedType =
        mapper.getTypeFactory().constructParametricType(wrapperType, types);
    return deserializeContainer(response, "results", wrappedType);
  }

  public static <T> List<T> deserializeContainer(
      Response response, String wrapper, Class<T> element) throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      final CollectionType collection =
          mapper.getTypeFactory().constructCollectionType(List.class, element);
      return fromJSON(
          response.getBody(), wrapper, mapper.getTypeFactory().constructType(collection));
    }

    throw deserializeException(response);
  }

  public static <T> T deserializeContainerSingleItem(Response response, Class<T> element)
      throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      return fromJSON(
          response.getBody(), "results", mapper.getTypeFactory().constructType(element));
    }

    throw deserializeException(response);
  }

  public static <T> T deserializeContainer(Response response, String wrapper, JavaType element)
      throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      final CollectionType collection =
          mapper.getTypeFactory().constructCollectionType(List.class, element);
      return fromJSON(
          response.getBody(), wrapper, mapper.getTypeFactory().constructType(collection));
    }

    throw deserializeException(response);
  }

  public static <T> T deserialize(Response response, String wrapper, Class<T> type)
      throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      return fromJSON(response.getBody(), wrapper, mapper.getTypeFactory().constructType(type));
    }

    throw deserializeException(response);
  }

  public static <T> T deserialize(Response response, Class<T> type)
      throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      return fromJSON(response.getBody(), type);
    }

    throw deserializeException(response);
  }

  public static <T> T deserialize(Response response, TypeReference<T> type)
      throws IOException, StreamException {
    if (normalResponseCodes.contains(response.getCode())) {
      return fromJSON(response.getBody(), type);
    }

    throw deserializeException(response);
  }

  public static Void deserializeError(Response response) throws IOException, StreamException {
    if (!normalResponseCodes.contains(response.getCode())) {
      throw deserializeException(response);
    }
    return null;
  }

  private static StreamAPIException deserializeException(Response response) throws IOException {
    // XXX: a hack to avoid reading empty stream
    try (PushbackInputStream wrapper = new PushbackInputStream(response.getBody())) {
      int read = wrapper.read();
      if (read == -1) {
        return new StreamAPIException(null, 0, response.getCode(), "API Error");
      }
      wrapper.unread(read);
      return fromJSON(wrapper, StreamAPIException.class);
    }
  }
}
