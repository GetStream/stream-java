package io.getstream.client;

import static io.getstream.core.utils.Serialization.*;

import com.fasterxml.jackson.core.type.TypeReference;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.*;
import io.getstream.core.options.*;
import io.getstream.core.utils.DefaultOptions;
import java.io.IOException;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public final class NotificationFeed extends Feed {
  NotificationFeed(Client client, FeedID id) {
    super(client, id);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities()
      throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(Limit limit)
      throws StreamException {
    return getActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(Offset offset)
      throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(Filter filter)
      throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      ActivityMarker marker) throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, Offset offset) throws StreamException {
    return getActivities(
        limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, Filter filter) throws StreamException {
    return getActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, ActivityMarker marker) throws StreamException {
    return getActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Filter filter, ActivityMarker marker) throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Offset offset, ActivityMarker marker) throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
    return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
  }

  public CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
    return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
  }

  CompletableFuture<PaginatedNotificationGroup<Activity>> getActivities(
      Limit limit, Offset offset, Filter filter, ActivityMarker marker) throws StreamException {
    return getClient()
        .getActivities(getID(), limit, offset, filter, marker)
        .thenApply(
            response -> {
              try {
                return deserialize(
                    response, new TypeReference<PaginatedNotificationGroup<Activity>>() {});
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(Class<T> type)
      throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit) throws StreamException {
    return getCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Offset offset) throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Filter filter) throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, ActivityMarker marker) throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset) throws StreamException {
    return getCustomActivities(
        type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, Filter filter) throws StreamException {
    return getCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
    return getCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
    return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
    return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker);
  }

  <T> CompletableFuture<PaginatedNotificationGroup<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset, Filter filter, ActivityMarker marker)
      throws StreamException {
    return getClient()
        .getActivities(getID(), limit, offset, filter, marker)
        .thenApply(
            response -> {
              try {
                return deserialize(response, new TypeReference<PaginatedNotificationGroup<T>>() {});
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities()
      throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Offset offset) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Filter filter) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset) throws StreamException {
    return getEnrichedActivities(
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Filter filter, ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Offset offset, ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
    return getEnrichedActivities(
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Filter filter, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Offset offset, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
  }

  public CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  CompletableFuture<PaginatedNotificationGroup<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, Filter filter, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getClient()
        .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
        .thenApply(
            response -> {
              try {
                return deserialize(
                    response, new TypeReference<PaginatedNotificationGroup<EnrichedActivity>>() {});
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_MARKER,
        flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, ActivityMarker marker, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, offset, DefaultOptions.DEFAULT_FILTER, DefaultOptions.DEFAULT_MARKER, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, filter, DefaultOptions.DEFAULT_MARKER, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, ActivityMarker marker) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        marker,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, filter, marker, flags);
  }

  public <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, ActivityMarker marker, EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, offset, DefaultOptions.DEFAULT_FILTER, marker, flags);
  }

  <T> CompletableFuture<PaginatedNotificationGroup<T>> getEnrichedCustomActivities(
      Class<T> type,
      Limit limit,
      Offset offset,
      Filter filter,
      ActivityMarker marker,
      EnrichmentFlags flags)
      throws StreamException {
    return getClient()
        .getEnrichedActivities(getID(), limit, offset, filter, marker, flags)
        .thenApply(
            response -> {
              try {
                return deserialize(response, new TypeReference<PaginatedNotificationGroup<T>>() {});
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }
}
