package io.getstream.core.utils;

import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Request.Builder;
import io.getstream.core.http.Token;
import io.getstream.core.options.RequestOption;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class Request {
  private Request() {
    /* nothing to see here */
  }

  public static Builder buildRequest(URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    final Builder builder =
        HTTPClient.requestBuilder().url(url).token(token).addQueryParameter("api_key", apiKey);
    for (RequestOption option : options) {
      option.apply(builder);
    }
    return builder;
  }

  public static io.getstream.core.http.Request buildGet(
      URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).get().build();
  }

  public static io.getstream.core.http.Request buildDelete(
      URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).delete().build();
  }

  public static io.getstream.core.http.Request buildPost(
      URL url, String apiKey, Token token, byte[] payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).post(payload).build();
  }

  public static io.getstream.core.http.Request buildMultiPartPost(
      URL url,
      String apiKey,
      Token token,
      String fileName,
      byte[] payload,
      RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).multiPartPost(fileName, payload).build();
  }

  public static io.getstream.core.http.Request buildMultiPartPost(
      URL url, String apiKey, Token token, File payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).multiPartPost(payload).build();
  }

  public static io.getstream.core.http.Request buildPut(
      URL url, String apiKey, Token token, byte[] payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).put(payload).build();
  }
}
