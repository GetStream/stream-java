package io.getstream.cloud;

import com.google.common.collect.Iterables;
import io.getstream.core.StreamAnalytics;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Engagement;
import io.getstream.core.models.Impression;
import io.getstream.core.utils.Auth.TokenAction;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static io.getstream.core.utils.Auth.buildAnalyticsRedirectToken;
import static io.getstream.core.utils.Auth.buildAnalyticsToken;

public final class CloudAnalyticsClient {
    private final Token token;
    private final StreamAnalytics analytics;

    CloudAnalyticsClient(Token token, StreamAnalytics analytics) {
        this.token = token;
        this.analytics = analytics;
    }

    public CompletableFuture<Void> trackEngagement(Iterable<Engagement> events) throws StreamException {
        return trackEngagement(Iterables.toArray(events, Engagement.class));
    }

    public CompletableFuture<Void> trackEngagement(Engagement... events) throws StreamException {
        return analytics.trackEngagement(token, events);
    }

    public CompletableFuture<Void> trackImpression(Impression event) throws StreamException {
        return analytics.trackImpression(token, event);
    }
}
