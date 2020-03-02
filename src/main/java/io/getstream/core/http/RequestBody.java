package io.getstream.core.http;

import com.google.common.base.MoreObjects;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public final class RequestBody {
  public enum Type {
    JSON("application/json"),
    MULTI_PART("multipart/form-data");

    private final String type;

    Type(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }
  }

  private final Type type;
  private final byte[] bytes;
  private final File file;
  private final String fileName;

  RequestBody(byte[] bytes, Type type) {
    this.type = type;
    this.bytes = bytes;
    this.file = null;
    this.fileName = null;
  }

  RequestBody(String fileName, byte[] bytes, Type type) {
    this.type = type;
    this.bytes = bytes;
    this.file = null;
    this.fileName = fileName;
  }

  RequestBody(File file, Type type) {
    this.type = type;
    this.bytes = null;
    this.file = file;
    this.fileName = file.getName();
  }

  public Type getType() {
    return type;
  }

  public byte[] getBytes() {
    return bytes;
  }

  public File getFile() {
    return file;
  }

  public String getFileName() {
    return fileName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RequestBody that = (RequestBody) o;
    return type == that.type && Arrays.equals(bytes, that.bytes) && Objects.equals(file, that.file);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(type, file);
    result = 31 * result + Arrays.hashCode(bytes);
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(RequestBody.class)
        .add("type", type)
        .add("bytes", bytes)
        .add("file", file)
        .toString();
  }
}
