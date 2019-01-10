package io.getstream.cloud;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.options.*;
import io.getstream.core.utils.DefaultOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static io.getstream.core.utils.Serialization.deserializeContainer;

public final class CloudFlatFeed extends CloudFeed {
    CloudFlatFeed(CloudClient client, FeedID id) {
        super(client, id);
    }

    public CompletableFuture<List<Activity>> getActivities() throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, null);
    }

    public CompletableFuture<List<Activity>> getActivities(String ranking) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, ranking);
    }

    public CompletableFuture<List<Activity>> getActivities(Filter filter) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_OFFSET, filter, null);
    }

    public CompletableFuture<List<Activity>> getActivities(Offset offset) throws StreamException {
        return getActivities(offset, DefaultOptions.DEFAULT_FILTER, null);
    }

    public CompletableFuture<List<Activity>> getActivities(Filter filter, String ranking) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_OFFSET, filter, ranking);
    }

    public CompletableFuture<List<Activity>> getActivities(Offset offset, String ranking) throws StreamException {
        return getActivities(offset, DefaultOptions.DEFAULT_FILTER, ranking);
    }

    CompletableFuture<List<Activity>> getActivities(Offset offset, Filter filter, String ranking) throws StreamException {
        final RequestOption[] options = ranking == null
                ? new RequestOption[]{offset, filter, DefaultOptions.DEFAULT_MARKER}
                : new RequestOption[]{offset, filter, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)};
        return getClient()
                .getActivities(getID(), options)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Activity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, null);
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, String ranking) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, ranking);
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, null);
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, null);
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Offset offset, String ranking) throws StreamException {
        return getCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, ranking);
    }

    public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Filter filter, String ranking) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, ranking);
    }

    <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Offset offset, Filter filter, String ranking) throws StreamException {
        final RequestOption[] options = ranking == null
                ? new RequestOption[]{offset, filter, DefaultOptions.DEFAULT_MARKER}
                : new RequestOption[]{offset, filter, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)};
        return getClient()
                .getActivities(getID(), options)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities() throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(String ranking) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Filter filter) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset) throws StreamException {
        return getEnrichedActivities(offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, ranking);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedActivities(offset, DefaultOptions.DEFAULT_FILTER, flags, null);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Filter filter, String ranking) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset, String ranking) throws StreamException {
        return getEnrichedActivities(offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
    }

    public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset, EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedActivities(offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
    }

    CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset, Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
        final RequestOption[] options = ranking == null
                ? new RequestOption[]{offset, filter, flags, DefaultOptions.DEFAULT_MARKER}
                : new RequestOption[]{offset, filter, flags, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)};
        return getClient()
                .getEnrichedActivities(getID(), options)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, EnrichedActivity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, ranking);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset) throws StreamException {
        return getEnrichedCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset, EnrichmentFlags flags) throws StreamException {
        return getEnrichedCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, flags, null);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset, EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Filter filter, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_ENRICHMENT_FLAGS, ranking);
    }

    public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
    }

    <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset, Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
        final RequestOption[] options = ranking == null
                ? new RequestOption[]{offset, filter, flags, DefaultOptions.DEFAULT_MARKER}
                : new RequestOption[]{offset, filter, flags, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)};
        return getClient()
                .getActivities(getID(), options)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
