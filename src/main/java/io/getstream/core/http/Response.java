package io.getstream.core.http;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import java.io.InputStream;
import java.util.Objects;

public final class Response {
  private final int code;
  private final InputStream body;

  public Response(int code, InputStream body) {
    checkArgument(code >= 100 && code <= 599, "Invalid HTTP status code");
    this.code = code;
    this.body = body;
  }

  public int getCode() {
    return code;
  }

  public InputStream getBody() {
    return body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Response response = (Response) o;
    return code == response.code && Objects.equals(body, response.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, body);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("code", this.code)
        .add("body", this.body)
        .toString();
  }
}
