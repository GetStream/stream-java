package io.getstream.client;

import static io.getstream.core.utils.Serialization.deserializeContainer;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.models.Activity;
import io.getstream.core.models.EnrichedActivity;
import io.getstream.core.models.FeedID;
import io.getstream.core.options.*;
import io.getstream.core.utils.DefaultOptions;

import java.io.IOException;
import java.util.List;

import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public final class FlatFeed extends Feed {
  FlatFeed(Client client, FeedID id) {
    super(client, id);
  }

  public CompletableFuture<List<Activity>> getActivities() throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        null);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit) throws StreamException {
    return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, null);
  }

  public CompletableFuture<List<Activity>> getActivities(String ranking) throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        ranking);
  }

  public CompletableFuture<List<Activity>> getActivities(Filter filter) throws StreamException {
    return getActivities(DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, null);
  }

  public CompletableFuture<List<Activity>> getActivities(Offset offset) throws StreamException {
    return getActivities(DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, null);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit, String ranking)
      throws StreamException {
    return getActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit, Filter filter)
      throws StreamException {
    return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, null);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit, Offset offset)
      throws StreamException {
    return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, null);
  }

  public CompletableFuture<List<Activity>> getActivities(Filter filter, String ranking)
      throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, ranking);
  }

  public CompletableFuture<List<Activity>> getActivities(Offset offset, String ranking)
      throws StreamException {
    return getActivities(
        DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit, Filter filter, String ranking)
      throws StreamException {
    return getActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, ranking);
  }

  public CompletableFuture<List<Activity>> getActivities(Limit limit, Offset offset, String ranking)
      throws StreamException {
    return getActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  CompletableFuture<List<Activity>> getActivities(
      Limit limit, Offset offset, Filter filter, String ranking) throws StreamException {
    final RequestOption[] options =
        ranking == null
            ? new RequestOption[] {limit, offset, filter, DefaultOptions.DEFAULT_MARKER}
            : new RequestOption[] {
              limit, offset, filter, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)
            };
    return getClient()
        .getActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, Activity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  CompletableFuture<List<Activity>> getActivities(
      Limit limit, Offset offset, Filter filter, String ranking, RankingVars rankingVars) throws StreamException {

    final RequestOption[] options =
        ranking == null
            ? new RequestOption[] {limit, offset, filter, DefaultOptions.DEFAULT_MARKER}
            : new RequestOption[] {
              limit, offset, filter, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking), rankingVars
            };
    return getClient()
        .getActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, Activity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public CompletableFuture<List<Activity>> getActivities(RequestOption... options)
      throws StreamException {
    // If no options provided, use defaults
    if (options == null || options.length == 0) {
      options = new RequestOption[] {
          DefaultOptions.DEFAULT_LIMIT,
          DefaultOptions.DEFAULT_OFFSET,
          DefaultOptions.DEFAULT_FILTER,
          DefaultOptions.DEFAULT_MARKER
      };
    }
    
    return getClient()
        .getActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, Activity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type) throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Limit limit)
      throws StreamException {
    return getCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, String ranking)
      throws StreamException {
    return getCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Filter filter)
      throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(Class<T> type, Offset offset)
      throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, String ranking) throws StreamException {
    return getCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, Filter filter) throws StreamException {
    return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset) throws StreamException {
    return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, null);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Offset offset, String ranking) throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Filter filter, String ranking) throws StreamException {
    return getCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, ranking);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset, String ranking) throws StreamException {
    return getCustomActivities(type, limit, offset, DefaultOptions.DEFAULT_FILTER, ranking);
  }

  public <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, Filter filter, String ranking) throws StreamException {
    return getCustomActivities(type, limit, DefaultOptions.DEFAULT_OFFSET, filter, ranking);
  }

  <T> CompletableFuture<List<T>> getCustomActivities(
      Class<T> type, Limit limit, Offset offset, Filter filter, String ranking)
      throws StreamException {
    final RequestOption[] options =
        ranking == null
            ? new RequestOption[] {limit, offset, filter, DefaultOptions.DEFAULT_MARKER}
            : new RequestOption[] {
              limit, offset, filter, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)
            };
    return getClient()
        .getActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, type);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities() throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Limit limit)
      throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(EnrichmentFlags flags)
      throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        flags,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(String ranking)
      throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, String ranking) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        flags,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(
        limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Filter filter)
      throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Limit limit, Filter filter)
      throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Offset offset)
      throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(Limit limit, Offset offset)
      throws StreamException {
    return getEnrichedActivities(
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Filter filter, String ranking) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, String ranking) throws StreamException {
    return getEnrichedActivities(
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(limit, DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Offset offset, String ranking) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, String ranking) throws StreamException {
    return getEnrichedActivities(
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Offset offset, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(
        DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedActivities(limit, offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public CompletableFuture<List<EnrichedActivity>> getEnrichedActivities(
      Limit limit, Offset offset, Filter filter, EnrichmentFlags flags, String ranking)
      throws StreamException {
    final RequestOption[] options =
        ranking == null
            ? new RequestOption[] {limit, offset, filter, flags, DefaultOptions.DEFAULT_MARKER}
            : new RequestOption[] {
              limit, offset, filter, flags, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)
            };
    return getClient()
        .getEnrichedActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, EnrichedActivity.class);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type)
      throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Limit limit)
      throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        flags,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, String ranking)
      throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        DefaultOptions.DEFAULT_FILTER,
        flags,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Filter filter)
      throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, filter, flags, null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(Class<T> type, Offset offset)
      throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, EnrichmentFlags flags) throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, offset, DefaultOptions.DEFAULT_FILTER, flags, null);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        offset,
        DefaultOptions.DEFAULT_FILTER,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Offset offset, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Offset offset, EnrichmentFlags flags, String ranking)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, offset, DefaultOptions.DEFAULT_FILTER, flags, ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        DefaultOptions.DEFAULT_LIMIT,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type,
        limit,
        DefaultOptions.DEFAULT_OFFSET,
        filter,
        DefaultOptions.DEFAULT_ENRICHMENT_FLAGS,
        ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Filter filter, EnrichmentFlags flags, String ranking) throws StreamException {
    return getEnrichedCustomActivities(
        type, DefaultOptions.DEFAULT_LIMIT, DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
  }

  public <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type, Limit limit, Filter filter, EnrichmentFlags flags, String ranking)
      throws StreamException {
    return getEnrichedCustomActivities(
        type, limit, DefaultOptions.DEFAULT_OFFSET, filter, flags, ranking);
  }

  <T> CompletableFuture<List<T>> getEnrichedCustomActivities(
      Class<T> type,
      Limit limit,
      Offset offset,
      Filter filter,
      EnrichmentFlags flags,
      String ranking)
      throws StreamException {
    final RequestOption[] options =
        ranking == null
            ? new RequestOption[] {limit, offset, filter, flags, DefaultOptions.DEFAULT_MARKER}
            : new RequestOption[] {
              limit, offset, filter, flags, DefaultOptions.DEFAULT_MARKER, new Ranking(ranking)
            };
    return getClient()
        .getActivities(getID(), options)
        .thenApply(
            response -> {
              try {
                return deserializeContainer(response, type);
              } catch (StreamException | IOException e) {
                throw new CompletionException(e);
              }
            });
  }
}
