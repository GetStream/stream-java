package io.getstream.cloud;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.models.Group;
import io.getstream.core.options.ActivityMarker;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Pagination;
import io.getstream.core.utils.DefaultOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static io.getstream.core.utils.Serialization.deserializeContainer;

public class CloudAggregatedFeed extends CloudFeed {
    CloudAggregatedFeed(CloudClient client, FeedID id) {
        super(client, id);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities() throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Pagination pagination) throws StreamException {
        return getActivities(pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Filter filter) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getActivities(DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Pagination pagination, ActivityMarker marker) throws StreamException {
        return getActivities(pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Pagination pagination, Filter filter) throws StreamException {
        return getActivities(pagination, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<Activity>>> getActivities(Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), pagination, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, Activity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Pagination pagination) throws StreamException {
        return getCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Pagination pagination, ActivityMarker marker) throws StreamException {
        return getCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Pagination pagination, Filter filter) throws StreamException {
        return getCustomActivities(type, pagination, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getCustomActivities(Class<T> type, Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getActivities(getID(), pagination, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities() throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Pagination pagination) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, ActivityMarker marker) throws StreamException {
        return getEnrichedActivities(pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, Filter filter) throws StreamException {
        return getEnrichedActivities(pagination, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public CompletableFuture<? extends List<? extends Group<EnrichedActivity>>> getEnrichedActivities(Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), pagination, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, EnrichedActivity.class);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, DefaultOptions.DEFAULT_PAGINATION, filter, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, ActivityMarker marker) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, DefaultOptions.DEFAULT_FILTER, marker);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, Filter filter) throws StreamException {
        return getEnrichedCustomActivities(type, pagination, filter, DefaultOptions.DEFAULT_MARKER);
    }

    public <T> CompletableFuture<? extends List<? extends Group<T>>> getEnrichedCustomActivities(Class<T> type, Pagination pagination, Filter filter, ActivityMarker marker) throws StreamException {
        return getClient()
                .getEnrichedActivities(getID(), pagination, filter, marker)
                .thenApply(response -> {
                    try {
                        return deserializeContainer(response, Group.class, type);
                    } catch (StreamException | IOException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
