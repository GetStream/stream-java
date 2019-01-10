package io.getstream.client;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Group;
import io.getstream.core.options.*;
import io.getstream.core.utils.DefaultOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static io.getstream.core.utils.Serialization.deserializeContainer;

public class AggregatedFeed extends Feed {
    AggregatedFeed(Client client, FeedID id) {
        super(client, id);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities() throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Offset offset) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Filter filter) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, Offset offset) throws StreamException {
        return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, Filter filter) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, ActivityMarker marker) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Offset offset, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Limit limit, Offset offset, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), limit, offset, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, Activity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset) throws StreamException {
        return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, Filter filter) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
    }

    <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Limit limit, Offset offset, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), limit, offset, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities() throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Offset offset) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Limit limit, Offset offset, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, EnrichedActivity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
    }

    <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Limit limit, Offset offset, Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
