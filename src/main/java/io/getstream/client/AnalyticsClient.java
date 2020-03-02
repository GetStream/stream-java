package io.getstream.client;

import static io.getstream.core.utils.Auth.buildAnalyticsRedirectToken;
import static io.getstream.core.utils.Auth.buildAnalyticsToken;

import com.google.common.collect.Iterables;
import io.getstream.core.StreamAnalytics;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Engagement;
import io.getstream.core.models.Impression;
import io.getstream.core.utils.Auth.TokenAction;
import java.net.URL;
import java8.util.concurrent.CompletableFuture;

public final class AnalyticsClient {
  private final String secret;
  private final StreamAnalytics analytics;

  AnalyticsClient(String secret, StreamAnalytics analytics) {
    this.secret = secret;
    this.analytics = analytics;
  }

  public CompletableFuture<Void> trackEngagement(Iterable<Engagement> events)
      throws StreamException {
    return trackEngagement(Iterables.toArray(events, Engagement.class));
  }

  public CompletableFuture<Void> trackEngagement(Engagement... events) throws StreamException {
    final Token token = buildAnalyticsToken(secret, TokenAction.WRITE);
    return analytics.trackEngagement(token, events);
  }

  public CompletableFuture<Void> trackImpression(Impression event) throws StreamException {
    final Token token = buildAnalyticsToken(secret, TokenAction.WRITE);
    return analytics.trackImpression(token, event);
  }

  public URL createRedirectURL(URL url, Engagement... engagements) throws StreamException {
    return createRedirectURL(url, new Impression[0], engagements);
  }

  public URL createRedirectURL(URL url, Impression... impressions) throws StreamException {
    return createRedirectURL(url, impressions, new Engagement[0]);
  }

  public URL createRedirectURL(
      URL url, Iterable<Impression> impressions, Iterable<Engagement> engagements)
      throws StreamException {
    return createRedirectURL(
        url,
        Iterables.toArray(impressions, Impression.class),
        Iterables.toArray(engagements, Engagement.class));
  }

  public URL createRedirectURL(URL url, Impression[] impressions, Engagement[] engagements)
      throws StreamException {
    final Token token = buildAnalyticsRedirectToken(secret);
    return analytics.createRedirectURL(token, url, impressions, engagements);
  }
}
