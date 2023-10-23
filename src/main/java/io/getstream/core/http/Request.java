package io.getstream.core.http;

import com.google.common.base.MoreObjects;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public final class Request {
  private final Token token;
  private final URL url;
  private final Method method;
  private final RequestBody body;

  private Request(Builder builder) throws MalformedURLException {
    token = builder.token;
    url = builder.uri.toURL();
    method = builder.method;
    body = builder.body;
  }

  public Token getToken() {
    return token;
  }

  public URL getURL() {
    return url;
  }

  public Method getMethod() {
    return method;
  }

  public RequestBody getBody() {
    return body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return Objects.equals(token, request.token)
        && Objects.equals(url, request.url)
        && method == request.method
        && Objects.equals(body, request.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, url, method, body);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("token", this.token)
        .add("url", this.url)
        .add("method", this.method)
        .add("body", this.body)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public enum Method {
    GET,
    POST,
    PUT,
    DELETE
  }

  public static final class Builder {
    private Token token;
    private URI uri;
    private StringBuilder query;
    private Method method;
    private RequestBody body;

    public Builder token(Token token) {
      this.token = token;
      return this;
    }

    public Builder url(URL url) throws URISyntaxException {
      uri = url.toURI();
      if (uri.getQuery() != null) {
        query = new StringBuilder(uri.getQuery());
      } else {
        query = new StringBuilder();
      }
      return this;
    }

    public Builder addQueryParameter(String key, String value) {
      if (query.length() > 0) {
        query.append('&');
      }
      query.append(key);
      query.append('=');
      query.append(value);
      return this;
    }

    public Builder get() {
      this.method = Method.GET;
      this.body = null;
      return this;
    }

    public Builder post(byte[] body) {
      this.method = Method.POST;
      this.body = new RequestBody(body, RequestBody.Type.JSON);
      return this;
    }

    public Builder multiPartPost(String fileName, byte[] body) {
      this.method = Method.POST;
      this.body = new RequestBody(fileName, body, RequestBody.Type.MULTI_PART);
      return this;
    }

    public Builder multiPartPost(File body) {
      this.method = Method.POST;
      this.body = new RequestBody(body, RequestBody.Type.MULTI_PART);
      return this;
    }

    public Builder put(byte[] body) {
      this.method = Method.PUT;
      this.body = new RequestBody(body, RequestBody.Type.JSON);
      return this;
    }

    public Builder delete() {
      this.method = Method.DELETE;
      this.body = null;
      return this;
    }

    public Request build() throws MalformedURLException, URISyntaxException {
      this.uri =
          new URI(
              uri.getScheme(),
              uri.getUserInfo(),
              uri.getHost(),
              uri.getPort(),
              uri.getPath(),
              query.toString(),
              null);
      return new Request(this);
    }
  }
} 
