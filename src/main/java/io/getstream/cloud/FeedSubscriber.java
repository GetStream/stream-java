package io.getstream.cloud;

import io.getstream.core.faye.subscription.ChannelSubscription;
import io.getstream.core.models.FeedID;
import java8.util.concurrent.CompletableFuture;

public interface FeedSubscriber {
  CompletableFuture<ChannelSubscription> subscribe(
      FeedID feedID, RealtimeMessageCallback messageCallback);
}
