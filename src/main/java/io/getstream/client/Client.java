package io.getstream.client;

import com.google.common.collect.Iterables;
import io.getstream.core.Region;
import io.getstream.core.Stream;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.OKHTTPClientAdapter;
import io.getstream.core.http.Response;
import io.getstream.core.http.Token;
import io.getstream.core.models.*;
import io.getstream.core.options.RequestOption;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.getstream.core.utils.Auth.*;

public final class Client {
    private final String secret;
    private final Stream stream;

    private Client(String key, String secret, URL baseURL, HTTPClient httpClient) {
        this.secret = secret;
        this.stream = new Stream(key, baseURL, httpClient);
    }

    public static Builder builder(String apiKey, String secret) {
        return new Builder(apiKey, secret);
    }

    public CompletableFuture<Activity> updateActivityByID(String id, Map<String, Object> set, Iterable<String> unset) throws StreamException {
        return updateActivityByID(id, set, Iterables.toArray(unset, String.class));
    }

    public CompletableFuture<Activity> updateActivityByID(ActivityUpdate update) throws StreamException {
        return updateActivityByID(update.getID(), update.getSet(), update.getUnset());
    }

    public CompletableFuture<Activity> updateActivityByID(String id, Map<String, Object> set, String[] unset) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.WRITE);
        return stream.updateActivityByID(token, id, set, unset);
    }

    public CompletableFuture<Activity> updateActivityByForeignID(ForeignIDTimePair foreignIDTimePair, Map<String, Object> set, Iterable<String> unset) throws StreamException {
        checkNotNull(foreignIDTimePair, "No activity to update");
        return updateActivityByForeignID(foreignIDTimePair.getForeignID(), foreignIDTimePair.getTime(), set, unset);
    }

    public CompletableFuture<Activity> updateActivityByForeignID(ForeignIDTimePair foreignIDTimePair, Map<String, Object> set, String[] unset) throws StreamException {
        checkNotNull(foreignIDTimePair, "No activity to update");
        return updateActivityByForeignID(foreignIDTimePair.getForeignID(), foreignIDTimePair.getTime(), set, unset);
    }

    public CompletableFuture<Activity> updateActivityByForeignID(String foreignID, Date timestamp, Map<String, Object> set, Iterable<String> unset) throws StreamException {
        return updateActivityByForeignID(foreignID, timestamp, set, Iterables.toArray(unset, String.class));
    }

    public CompletableFuture<Activity> updateActivityByForeignID(ActivityUpdate update) throws StreamException {
        return updateActivityByForeignID(update.getForeignID(), update.getTime(), update.getSet(), update.getUnset());
    }

    public CompletableFuture<Activity> updateActivityByForeignID(String foreignID, Date timestamp, Map<String, Object> set, String[] unset) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.WRITE);
        return stream.updateActivityByForeignID(token, foreignID, timestamp, set, unset);
    }

    public CompletableFuture<OGData> openGraph(URL url) throws StreamException {
        final Token token = buildOpenGraphToken(secret);
        return stream.openGraph(token, url);
    }

    public CompletableFuture<List<Activity>> updateActivitiesByID(Iterable<ActivityUpdate> updates) throws StreamException {
        return updateActivitiesByID(Iterables.toArray(updates, ActivityUpdate.class));
    }

    public CompletableFuture<List<Activity>> updateActivitiesByID(ActivityUpdate... updates) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.WRITE);
        return stream.updateActivitiesByID(token, updates);
    }

    public CompletableFuture<List<Activity>> updateActivitiesByForeignID(Iterable<ActivityUpdate> updates) throws StreamException {
        return updateActivitiesByForeignID(Iterables.toArray(updates, ActivityUpdate.class));
    }

    public CompletableFuture<List<Activity>> updateActivitiesByForeignID(ActivityUpdate... updates) throws StreamException {
        final Token token = buildActivityToken(secret, TokenAction.WRITE);
        return stream.updateActivitiesByForeignID(token, updates);
    }

    public static final class Builder {
        private static final String DEFAULT_HOST = "stream-io-api.com";

        private final String apiKey;
        private final String secret;
        private HTTPClient httpClient;

        private String scheme = "https";
        private String region = Region.US_EAST.toString();
        private String host = DEFAULT_HOST;
        private int port = 443;

        public Builder(String apiKey, String secret) {
            checkNotNull(apiKey, "API key can't be null");
            checkNotNull(secret, "Secret can't be null");
            checkArgument(!apiKey.isEmpty(), "API key can't be empty");
            checkArgument(!secret.isEmpty(), "Secret can't be empty");
            this.apiKey = apiKey;
            this.secret = secret;
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

        private String buildHost() {
            final StringBuilder sb = new StringBuilder();
            if (host.equals(DEFAULT_HOST)) {
                sb.append(region).append(".");
            }
            sb.append(host);
            return sb.toString();
        }

        public Client build() throws MalformedURLException {
            if (httpClient == null) {
                httpClient = new OKHTTPClientAdapter();
            }
            return new Client(apiKey, secret, new URL(scheme, buildHost(), port, ""), httpClient);
        }
    }

    public <T> T getHTTPClientImplementation() {
        return stream.getHTTPClientImplementation();
    }

    public Token frontendToken(String userID) {
        return buildFrontendToken(secret, userID);
    }

    public FlatFeed flatFeed(FeedID id) {
        return new FlatFeed(this, id);
    }

    public FlatFeed flatFeed(String slug, String userID) {
        return flatFeed(new FeedID(slug, userID));
    }

    public AggregatedFeed aggregatedFeed(FeedID id) {
        return new AggregatedFeed(this, id);
    }

    public AggregatedFeed aggregatedFeed(String slug, String userID) {
        return aggregatedFeed(new FeedID(slug, userID));
    }

    public NotificationFeed notificationFeed(FeedID id) {
        return new NotificationFeed(this, id);
    }

    public NotificationFeed notificationFeed(String slug, String userID) {
        return notificationFeed(new FeedID(slug, userID));
    }

    public User user(String userID) {
        return new User(this, userID);
    }

    public BatchClient batch() {
        return new BatchClient(secret, stream.batch());
    }

    public CollectionsClient collections() {
        return new CollectionsClient(secret, stream.collections());
    }

    public PersonalizationClient personalization() {
        return new PersonalizationClient(secret, stream.personalization());
    }

    public AnalyticsClient analytics() {
        return new AnalyticsClient(secret, stream.analytics());
    }

    public ReactionsClient reactions() {
        return new ReactionsClient(secret, stream.reactions());
    }

    public FileStorageClient files() {
        return new FileStorageClient(secret, stream.files());
    }

    public ImageStorageClient images() {
        return new ImageStorageClient(secret, stream.images());
    }

    CompletableFuture<Response> getActivities(FeedID feed, RequestOption... options) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.READ);
        return stream.getActivities(token, feed, options);
    }

    CompletableFuture<Response> getEnrichedActivities(FeedID feed, RequestOption... options) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.READ);
        return stream.getEnrichedActivities(token, feed, options);
    }

    CompletableFuture<Response> addActivity(FeedID feed, Activity activity) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.WRITE);
        return stream.addActivity(token, feed, activity);
    }

    CompletableFuture<Response> addActivities(FeedID feed, Activity... activities) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.WRITE);
        return stream.addActivities(token, feed, activities);
    }

    CompletableFuture<Response> removeActivityByID(FeedID feed, String id) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.DELETE);
        return stream.removeActivityByID(token, feed, id);
    }

    CompletableFuture<Response> removeActivityByForeignID(FeedID feed, String foreignID) throws StreamException {
        final Token token = buildFeedToken(secret, feed, TokenAction.DELETE);
        return stream.removeActivityByForeignID(token, feed, foreignID);
    }

    CompletableFuture<Response> follow(FeedID source, FeedID target, int activityCopyLimit) throws StreamException {
        final Token token = buildFollowToken(secret, source, TokenAction.WRITE);
        final Token targetToken = buildFeedToken(secret, target, TokenAction.READ);
        return stream.follow(token, targetToken, source, target, activityCopyLimit);
    }

    CompletableFuture<Response> getFollowers(FeedID feed, RequestOption... options) throws StreamException {
        final Token token = buildFollowToken(secret, feed, TokenAction.READ);
        return stream.getFollowers(token, feed, options);
    }

    CompletableFuture<Response> getFollowed(FeedID feed, RequestOption... options) throws StreamException {
        final Token token = buildFollowToken(secret, feed, TokenAction.READ);
        return stream.getFollowed(token, feed, options);
    }

    CompletableFuture<Response> unfollow(FeedID source, FeedID target, RequestOption... options) throws StreamException {
        final Token token = buildFollowToken(secret, source, TokenAction.DELETE);
        return stream.unfollow(token, source, target, options);
    }

    CompletableFuture<Response> updateActivityToTargets(FeedID feed, Activity activity, FeedID[] add, FeedID[] remove, FeedID[] newTargets) throws StreamException {
        final Token token = buildToTargetUpdateToken(secret, feed, TokenAction.WRITE);
        return stream.updateActivityToTargets(token, feed, activity, add, remove, newTargets);
    }

    CompletableFuture<Response> getUser(String id) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.READ);
        return stream.getUser(token, id, false);
    }

    CompletableFuture<Response> deleteUser(String id) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.DELETE);
        return stream.deleteUser(token, id);
    }

    CompletableFuture<Response> getOrCreateUser(String id, Data data) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.WRITE);
        return stream.createUser(token, id, data, true);
    }

    CompletableFuture<Response> createUser(String id, Data data) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.WRITE);
        return stream.createUser(token, id, data, false);
    }

    CompletableFuture<Response> updateUser(String id, Data data) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.WRITE);
        return stream.updateUser(token, id, data);
    }

    CompletableFuture<Response> userProfile(String id) throws StreamException {
        final Token token = buildUsersToken(secret, TokenAction.READ);
        return stream.getUser(token, id, true);
    }
}
