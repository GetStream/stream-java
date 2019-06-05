package io.getstream.client;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.NotificationGroup;
import io.getstream.core.options.*;
import io.getstream.core.utils.DefaultOptions;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

import java.io.IOException;
import java.util.List;

import static io.getstream.core.utils.Serialization.deserializeContainer;

public final class NotificationFeed extends AggregatedFeed {
    NotificationFeed(Client client, FeedID id) {
        super(client, id);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities() throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Offset offset) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Filter filter) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, Offset offset) throws StreamException {
        return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, Filter filter) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, ActivityMarker marker) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Offset offset, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Limit limit, Offset offset, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), limit, offset, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, NotificationGroup.class, Activity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset) throws StreamException {
        return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, Filter filter) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), limit, offset, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, NotificationGroup.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities() throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Offset offset) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, NotificationGroup.class, EnrichedActivity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, NotificationGroup.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
