package io.getstream.cloud;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.NotificationGroup;
import io.getstream.core.options.ActivityMarker;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Pagination;
import io.getstream.core.utils.DefaultOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static io.getstream.core.utils.Serialization.deserializeContainer;

public final class CloudNotificationFeed extends CloudAggregatedFeed {
    CloudNotificationFeed(CloudClient client, FeedID id) {
        super(client, id);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities() throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Pagination pagination) throws StreamException {
        return getActivities(pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Filter filter) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Pagination pagination, ActivityMarker marker) throws StreamException {
        return getActivities(pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    CompletableFuture<List<NotificationGroup<Activity>>> getActivities(Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), pagination, filter, marker)
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
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Pagination pagination) throws StreamException {
        return getCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Pagination pagination, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    @Override
    <T> CompletableFuture<List<NotificationGroup<T>>> getCustomActivities(Class<T> type, Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), pagination, filter, marker)
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
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Pagination pagination) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, marker, flags);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    CompletableFuture<List<NotificationGroup<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), pagination, filter, marker, flags)
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
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, marker, flags);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    @Override
    public <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    @Override
    <T> CompletableFuture<List<NotificationGroup<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), pagination, filter, marker, flags)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, NotificationGroup.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
