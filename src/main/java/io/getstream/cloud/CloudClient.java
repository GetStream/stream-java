package io.getstream.cloud;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.getstream.core.Region;
import io.getstream.core.Stream;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.faye.DefaultMessageTransformer;
import io.getstream.core.faye.Message;
import io.getstream.core.faye.client.FayeClient;
import io.getstream.core.faye.subscription.ChannelSubscription;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import io.getstream.core.models.Activity;
import io.getstream.core.models.Data;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.OGData;
import io.getstream.core.models.RealtimeMessage;
import io.getstream.core.options.RequestOption;
import io.getstream.core.utils.Serialization;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java8.util.concurrent.CompletableFuture;

public final class CloudClient {
  private final String apiKey;
  private final Token token;
  private final String appID;
  private final String userID;
  private final Stream stream;
  private final FayeClient faye;

  private CloudClient(
      String key,
      Token token,
      String userID,
      String appID,
      URL baseURL,
      HTTPClient httpClient,
      URL fayeURL) {
    this.apiKey = key;
    this.token = token;
    this.appID = appID;
    this.userID = userID;
    this.stream = new Stream(key, baseURL, httpClient);
    this.faye = new FayeClient(fayeURL);
    this.faye.setMessageTransformer(new FayeMessageTransformer());
  }

  public static Builder builder(String apiKey, String token, String userID) {
    return new Builder(apiKey, new Token(token), userID);
  }

  public static Builder builder(String apiKey, Token token, String userID) {
    return new Builder(apiKey, token, userID);
  }

  public static Builder builder(String apiKey, Token token, String userID, String appID) {
    return new Builder(apiKey, token, userID, appID);
  }

  public static final class Builder {
    private static final String DEFAULT_HOST = "stream-io-api.com";
    private static final String DEFAULT_FAYE_URL = "https://faye-us-east.stream-io-api.com/faye";

    private final String apiKey;
    private final Token token;
    private final String userID;
    private final String appID;
    private HTTPClient httpClient;

    private String scheme = "https";
    private String region = Region.US_EAST.toString();
    private String host = DEFAULT_HOST;
    private int port = 443;
    private String fayeURL = DEFAULT_FAYE_URL;

    public Builder(String apiKey, Token token, String userID) {
      checkNotNull(apiKey, "API key can't be null");
      checkNotNull(token, "Token can't be null");
      checkNotNull(userID, "User ID can't be null");
      checkArgument(!apiKey.isEmpty(), "API key can't be empty");
      checkArgument(!userID.isEmpty(), "User ID can't be empty");
      this.apiKey = apiKey;
      this.token = token;
      this.userID = userID;
      this.appID = null;
    }

    public Builder(String apiKey, Token token, String userID, String appID) {
      checkNotNull(apiKey, "API key can't be null");
      checkNotNull(token, "Token can't be null");
      checkNotNull(userID, "User ID can't be null");
      checkArgument(!apiKey.isEmpty(), "API key can't be empty");
      checkArgument(!userID.isEmpty(), "User ID can't be empty");
      this.apiKey = apiKey;
      this.token = token;
      this.userID = userID;
      this.appID = appID;
    }

    public Builder httpClient(HTTPClient httpClient) {
      checkNotNull(httpClient, "HTTP client can't be null");
      this.httpClient = httpClient;
      return this;
    }

    public Builder scheme(String scheme) {
      checkNotNull(scheme, "Scheme can't be null");
      checkArgument(!scheme.isEmpty(), "Scheme can't be empty");
      this.scheme = scheme;
      return this;
    }

    public Builder host(String host) {
      checkNotNull(host, "Host can't be null");
      checkArgument(!host.isEmpty(), "Host can't be empty");
      this.host = host;
      return this;
    }

    public Builder port(int port) {
      checkArgument(port > 0, "Port has to be a non-zero positive number");
      this.port = port;
      return this;
    }

    public Builder region(Region region) {
      checkNotNull(region, "Region can't be null");
      this.region = region.toString();
      return this;
    }

    public Builder region(String region) {
      checkNotNull(region, "Region can't be null");
      checkArgument(!region.isEmpty(), "Region can't be empty");
      this.region = region;
      return this;
    }

    public Builder fayeURL(String fayeURL) {
      checkNotNull(fayeURL, "FayeUrl can't be null");
      checkArgument(!fayeURL.isEmpty(), "FayeUrl can't be empty");
      this.fayeURL = fayeURL;
      return this;
    }

    private String buildHost() {
      final StringBuilder sb = new StringBuilder();
      if (host.equals(DEFAULT_HOST)) {
        sb.append(region).append(".");
      }
      sb.append(host);
      return sb.toString();
    }

    public CloudClient build() throws MalformedURLException {
      if (httpClient == null) {
        httpClient = new OKHTTPClientAdapter();
      }

      return new CloudClient(
          apiKey,
          token,
          userID,
          appID,
          new URL(scheme, buildHost(), port, ""),
          httpClient,
          new URL(DEFAULT_FAYE_URL));
    }
  }

  private static class FeedSubscription {
    private String token;
    private String userId;
    private ChannelSubscription channelSubscription;

    private FeedSubscription(String token, String userId) {
      this.token = token;
      this.userId = userId;
    }

    private FeedSubscription(String token, String userId, ChannelSubscription subscription) {
      this.token = token;
      this.userId = userId;
      this.channelSubscription = subscription;
    }
  }

  private final Map<String, FeedSubscription> feedSubscriptions = new HashMap<>();

  private class FayeMessageTransformer extends DefaultMessageTransformer {
    @Override
    public Message transformRequest(Message message) {
      final String subscription = message.getSubscription();
      if (feedSubscriptions.containsKey(subscription)) {
        final FeedSubscription feedSubscription = feedSubscriptions.get(subscription);
        final Map<String, Object> ext = new HashMap<>();
        ext.put("user_id", feedSubscription.userId);
        ext.put("api_key", apiKey);
        ext.put("signature", feedSubscription.token);
        message.setExt(ext);
      }
      return message;
    }
  }

  public <T> T getHTTPClientImplementation() {
    return stream.getHTTPClientImplementation();
  }

  public CompletableFuture<OGData> openGraph(URL url) throws StreamException {
    return stream.openGraph(token, url);
  }

  private CompletableFuture<ChannelSubscription> feedSubscriber(
      FeedID feedId, RealtimeMessageCallback messageCallback) {
    final CompletableFuture<ChannelSubscription> subscriberCompletion = new CompletableFuture<>();
    try {
      checkNotNull(appID, "Missing app id, which is needed in order to subscribe feed");
      final String claim = feedId.getClaim();
      final String notificationChannel = "site" + "-" + appID + "-" + "feed" + "-" + claim;
      final FeedSubscription subscription =
          new FeedSubscription(token.toString(), notificationChannel);
      feedSubscriptions.put("/" + notificationChannel, subscription);

      final ChannelSubscription channelSubscription =
          faye.subscribe(
                  "/" + notificationChannel,
                  data -> {
                    try {
                      final byte[] payload = Serialization.toJSON(data);
                      final RealtimeMessage message =
                          Serialization.fromJSON(new String(payload), RealtimeMessage.class);
                      messageCallback.onMessage(message);
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  },
                  () -> feedSubscriptions.remove("/" + notificationChannel))
              .get();

      subscription.channelSubscription = channelSubscription;
      feedSubscriptions.put("/" + notificationChannel, subscription);
      subscriberCompletion.complete(channelSubscription);
    } catch (Exception e) {
      subscriberCompletion.completeExceptionally(e);
    }
    return subscriberCompletion;
  }

  // TODO: add personalized feed versions
  public CloudFlatFeed flatFeed(String slug) {
    return flatFeed(slug, userID);
  }

  public CloudFlatFeed flatFeed(String slug, CloudUser user) {
    return flatFeed(slug, user.getID());
  }

  public CloudFlatFeed flatFeed(String slug, String userID) {
    return flatFeed(new FeedID(slug, userID));
  }

  public CloudFlatFeed flatFeed(FeedID id) {
    return new CloudFlatFeed(this, id, this::feedSubscriber);
  }

  public CloudAggregatedFeed aggregatedFeed(String slug) {
    return aggregatedFeed(slug, userID);
  }

  public CloudAggregatedFeed aggregatedFeed(String slug, CloudUser user) {
    return aggregatedFeed(slug, user.getID());
  }

  public CloudAggregatedFeed aggregatedFeed(String slug, String userID) {
    return aggregatedFeed(new FeedID(slug, userID));
  }

  public CloudAggregatedFeed aggregatedFeed(FeedID id) {
    return new CloudAggregatedFeed(this, id, this::feedSubscriber);
  }

  public CloudNotificationFeed notificationFeed(String slug) {
    return notificationFeed(slug, userID);
  }

  public CloudNotificationFeed notificationFeed(String slug, CloudUser user) {
    return notificationFeed(slug, user.getID());
  }

  public CloudNotificationFeed notificationFeed(String slug, String userID) {
    return notificationFeed(new FeedID(slug, userID));
  }

  public CloudNotificationFeed notificationFeed(FeedID id) {
    return new CloudNotificationFeed(this, id, this::feedSubscriber);
  }

  public CloudUser user(String userID) {
    return new CloudUser(this, userID);
  }

  public CloudAnalyticsClient analytics() {
    return new CloudAnalyticsClient(token, stream.analytics());
  }

  public CloudCollectionsClient collections() {
    return new CloudCollectionsClient(token, userID, stream.collections());
  }

  public CloudReactionsClient reactions() {
    return new CloudReactionsClient(token, userID, stream.reactions());
  }

  public CloudFileStorageClient files() {
    return new CloudFileStorageClient(token, stream.files());
  }

  public CloudImageStorageClient images() {
    return new CloudImageStorageClient(token, stream.images());
  }

  CompletableFuture<Response> getActivities(FeedID feed, RequestOption... options)
      throws StreamException {
    return stream.getActivities(token, feed, options);
  }

  CompletableFuture<Response> getEnrichedActivities(FeedID feed, RequestOption... options)
      throws StreamException {
    return stream.getEnrichedActivities(token, feed, options);
  }

  CompletableFuture<Response> addActivity(FeedID feed, Activity activity) throws StreamException {
    return stream.addActivity(token, feed, activity);
  }

  CompletableFuture<Response> addActivities(FeedID feed, Activity... activities)
      throws StreamException {
    return stream.addActivities(token, feed, activities);
  }

  CompletableFuture<Response> removeActivityByID(FeedID feed, String id) throws StreamException {
    return stream.removeActivityByID(token, feed, id);
  }

  CompletableFuture<Response> removeActivityByForeignID(FeedID feed, String foreignID)
      throws StreamException {
    return stream.removeActivityByForeignID(token, feed, foreignID);
  }

  CompletableFuture<Response> follow(FeedID source, FeedID target, int activityCopyLimit)
      throws StreamException {
    return stream.follow(token, token, source, target, activityCopyLimit);
  }

  CompletableFuture<Response> getFollowers(FeedID feed, RequestOption... options)
      throws StreamException {
    return stream.getFollowers(token, feed, options);
  }

  CompletableFuture<Response> getFollowed(FeedID feed, RequestOption... options)
      throws StreamException {
    return stream.getFollowed(token, feed, options);
  }

  CompletableFuture<Response> unfollow(FeedID source, FeedID target, RequestOption... options)
      throws StreamException {
    return stream.unfollow(token, source, target, options);
  }

  CompletableFuture<Response> getUser(String id) throws StreamException {
    return stream.getUser(token, id, false);
  }

  CompletableFuture<Response> deleteUser(String id) throws StreamException {
    return stream.deleteUser(token, id);
  }

  CompletableFuture<Response> getOrCreateUser(String id, Data data) throws StreamException {
    return stream.createUser(token, id, data, true);
  }

  CompletableFuture<Response> createUser(String id, Data data) throws StreamException {
    return stream.createUser(token, id, data, false);
  }

  CompletableFuture<Response> updateUser(String id, Data data) throws StreamException {
    return stream.updateUser(token, id, data);
  }

  CompletableFuture<Response> userProfile(String id) throws StreamException {
    return stream.getUser(token, id, true);
  }
}
