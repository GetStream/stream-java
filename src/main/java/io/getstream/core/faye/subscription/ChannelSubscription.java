package io.getstream.core.faye.subscription;

import io.getstream.core.faye.Message;
import io.getstream.core.faye.client.FayeClient;

public class ChannelSubscription {
  private final FayeClient client;
  private final String channel;
  private final ChannelDataCallback channelDataCallback;
  private final SubscriptionCancelledCallback onCancelledCallback;
  private WithChannelDataCallback withChannel;

  private boolean cancelled = false;

  public ChannelSubscription(FayeClient client, String channel) {
    this.client = client;
    this.channel = channel;
    this.channelDataCallback = null;
    this.onCancelledCallback = null;
  }

  public ChannelSubscription(
      FayeClient client,
      String channel,
      ChannelDataCallback channelDataCallback,
      SubscriptionCancelledCallback onCancelledCallback) {
    this.client = client;
    this.channel = channel;
    this.channelDataCallback = channelDataCallback;
    this.onCancelledCallback = onCancelledCallback;
  }

  public ChannelSubscription setWithChannel(WithChannelDataCallback withChannel) {
    this.withChannel = withChannel;
    return this;
  }

  public void call(Message message) {
    if (channelDataCallback != null) channelDataCallback.onData(message.getData());
    if (withChannel != null) withChannel.onData(message.getChannel(), message.getData());
  }

  public void cancel() {
    if (cancelled) return;
    client.unsubscribe(channel, this);
    if (onCancelledCallback != null) onCancelledCallback.onCancelled();
    cancelled = true;
  }
}
