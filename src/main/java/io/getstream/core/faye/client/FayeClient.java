package io.getstream.core.faye.client;

import io.getstream.core.faye.Advice;
import io.getstream.core.faye.Channel;
import io.getstream.core.faye.DefaultMessageTransformer;
import io.getstream.core.faye.FayeClientError;
import io.getstream.core.faye.Message;
import io.getstream.core.faye.MessageTransformer;
import io.getstream.core.faye.subscription.ChannelDataCallback;
import io.getstream.core.faye.subscription.ChannelSubscription;
import io.getstream.core.faye.subscription.SubscriptionCancelledCallback;
import io.getstream.core.utils.Serialization;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class FayeClient extends WebSocketListener {

  private static final String BAYEUX_VERSION = "1.0";
  private static final int DEFAULT_TIMEOUT = 60; // seconds
  private static final int DEFAULT_INTERVAL = 0; // seconds

  private final String baseURL;
  private final int timeout;
  private final int interval;

  private Advice advice;

  public FayeClient(URL baseURL) {
    String url = baseURL.toString();
    if (url.startsWith("https")) {
      url = url.replace("https", "wss");
    } else if (url.startsWith("http")) {
      url = url.replace("http", "ws");
    }

    this.baseURL = url;
    this.timeout = DEFAULT_TIMEOUT;
    this.interval = DEFAULT_INTERVAL;
    this.advice = new Advice(Advice.RETRY, 1000 * interval, 1000 * timeout);
  }

  private String clientId;
  private final Map<String, Channel> channels = new HashMap<>();
  private final Map<String, MessageCallback> responseCallbacks = new HashMap<>();

  private MessageTransformer messageTransformer = new DefaultMessageTransformer();

  private FayeErrorListener errorListener;

  public void setMessageTransformer(MessageTransformer messageTransformer) {
    this.messageTransformer = messageTransformer;
  }

  private FayeClientState state = FayeClientState.UNCONNECTED;

  private void setState(FayeClientState state) {
    this.state = state;
    if (stateChangeListener != null) stateChangeListener.onStateChanged(state);
  }

  private StateChangeListener stateChangeListener;

  public void setStateChangeListener(StateChangeListener stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }

  private WebSocket webSocket;
  private final OkHttpClient httpClient = new OkHttpClient();

  private Timer timer = new Timer();

  private void initWebSocket() {
    // Initiating connection with $baseUrl
    if (webSocket != null) {
      closeWebSocket();
    }
    final Request request = new Request.Builder().url(baseURL).build();
    webSocket = httpClient.newWebSocket(request, this);
  }

  private void closeWebSocket() {
    // Cancelling all timer tasks
    if (timer != null) {
      timer.cancel();
      timer = null;
    }

    // Closing connection for $baseUrl
    if (webSocket != null) {
      webSocket.close(1000, "Connection closed by client");
      webSocket = null;
    }
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    List<Message> messages = null;
    try {
      messages = Serialization.fromJSONList(text, Message.class);
    } catch (IOException ignored) {
    }

    if (messages == null) return;

    for (Message message : messages) {
      receiveMessage(message);
    }
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    // 'Error occurred', error, stacktrace);
    closeWebSocket();
    initWebSocket();

    if (errorListener != null) {
      errorListener.onError(t, response);
    }
  }

  private boolean manuallyClosed = false;

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    closeWebSocket();

    // Checking if we manually closed the connection
    if (manuallyClosed) return;
    initWebSocket();
  }

  private void scheduleTimerTask(Callback callback, long duration) {
    try {
      if (timer == null) timer = new Timer();
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              callback.call();
            }
          },
          duration);
    } catch (Exception ignored) {
      // We don't really care if the timer is cancelled, we create a new client anyway.
    }
  }

  public void handshake() {
    handshake(null);
  }

  private void handshake(Callback callback) {
    if (Objects.equals(advice.getReconnect(), Advice.NONE)) return;
    if (state != FayeClientState.UNCONNECTED) return;

    setState(FayeClientState.CONNECTING);

    initWebSocket();

    // Initiating handshake with $baseUrl

    final String[] connectionTypes = {"websocket"};
    final Message message = new Message(Channel.HANDSHAKE);
    message.setVersion(BAYEUX_VERSION);
    message.setSupportedConnectionTypes(connectionTypes);
    sendMessage(
        message,
        response -> {
          if (response.isSuccessful() != null && response.isSuccessful()) {
            setState(FayeClientState.CONNECTED);
            clientId = response.getClientId();

            // Handshake successful: $clientId
            final Set<String> keys = channels.keySet();
            subscribeChannels(keys.toArray(new String[0]));
            if (callback != null) callback.call();
          } else {
            // Handshake unsuccessful
            scheduleTimerTask(() -> handshake(callback), 1000);
            setState(FayeClientState.UNCONNECTED);
          }
        });
  }

  private boolean connectRequestInProgress = false;

  public void connect() {
    connect(null);
  }

  private void connect(Callback callback) {
    if (Objects.equals(advice.getReconnect(), Advice.NONE)) return;
    if (state == FayeClientState.DISCONNECTED) return;

    if (state == FayeClientState.UNCONNECTED) {
      handshake(() -> connect(callback));
      return;
    }

    if (callback != null) callback.call();
    if (state != FayeClientState.CONNECTED) return;

    if (connectRequestInProgress) return;
    connectRequestInProgress = true;

    // Initiating connection for $clientId

    final Message message = new Message(Channel.CONNECT);
    message.setClientId(clientId);
    message.setConnectionType("websocket");
    sendMessage(message, response -> cycleConnection());
  }

  public CompletableFuture<Void> disconnect() {
    final CompletableFuture<Void> disconnectionCompleter = new CompletableFuture<>();

    if (state != FayeClientState.CONNECTED) disconnectionCompleter.complete(null);
    setState(FayeClientState.DISCONNECTED);

    // Disconnecting $clientId

    final Message message = new Message(Channel.DISCONNECT);
    message.setClientId(clientId);
    sendMessage(
        message,
        response -> {
          if (response.isSuccessful() != null && response.isSuccessful()) {
            manuallyClosed = true;
            closeWebSocket();
            disconnectionCompleter.complete(null);
          } else {
            final FayeClientError error = FayeClientError.parse(response.getError());
            disconnectionCompleter.completeExceptionally(error);
          }
        });

    // Clearing channel listeners for $clientId
    channels.clear();

    return disconnectionCompleter;
  }

  private void subscribeChannels(String[] channels) {
    for (String channel : channels) {
      subscribe(channel, true);
    }
  }

  public CompletableFuture<ChannelSubscription> subscribe(
      String channel, ChannelDataCallback callback) {
    return subscribe(channel, callback, null,null, null);
  }

  private CompletableFuture<ChannelSubscription> subscribe(String channel, Boolean force) {
    return subscribe(channel, null, null, null, force);
  }

  public CompletableFuture<ChannelSubscription> subscribe(
      String channel, ChannelDataCallback callback, SubscriptionCancelledCallback onCancelled, FayeErrorListener errorListener) {
    return subscribe(channel, callback, onCancelled, errorListener, null);
  }

  private CompletableFuture<ChannelSubscription> subscribe(
      String channel,
      ChannelDataCallback onData,
      SubscriptionCancelledCallback onCancelled,
      FayeErrorListener errorListener,
      Boolean force) {
    this.errorListener = errorListener;
    // default value
    if (force == null) force = false;

    final CompletableFuture<ChannelSubscription> subscriptionCompleter = new CompletableFuture<>();

    final ChannelSubscription channelSubscription =
        new ChannelSubscription(this, channel, onData, onCancelled);
    final boolean hasSubscribe = channels.containsKey(channel);

    if (hasSubscribe && !force) {
      subscribeChannel(channel, channelSubscription);
      subscriptionCompleter.complete(channelSubscription);
    } else {
      Boolean finalForce = force;
      connect(
          () -> {
            // Client $clientId attempting to subscribe to $channel
            if (!finalForce) subscribeChannel(channel, channelSubscription);
            final Message message = new Message(Channel.SUBSCRIBE);
            message.setClientId(clientId);
            message.setSubscription(channel);
            sendMessage(
                message,
                response -> {
                  if (response.isSuccessful() != null && response.isSuccessful()) {
                    final String subscribedChannel = response.getSubscription();
                    // Subscription acknowledged for $channel to $clientId
                    subscriptionCompleter.complete(channelSubscription);
                  } else {
                    unsubscribeChannel(channel, channelSubscription);
                    final FayeClientError error = FayeClientError.parse(response.getError());
                    subscriptionCompleter.completeExceptionally(error);
                  }
                });
          });
    }

    return subscriptionCompleter;
  }

  public void unsubscribe(String channel, ChannelSubscription channelSubscription) {
    final boolean dead = unsubscribeChannel(channel, channelSubscription);
    if (!dead) return;

    connect(
        () -> {
          // Client $clientId attempting to unsubscribe from $channel
          final Message message = new Message(Channel.UNSUBSCRIBE);
          message.setClientId(clientId);
          message.setSubscription(channel);
          sendMessage(
              message,
              response -> {
                if (response.isSuccessful() != null && response.isSuccessful()) {
                  final String unsubscribedChannel = response.getSubscription();
                  // Un-subscription acknowledged for $clientId from $channel
                }
              });
        });
  }

  public CompletableFuture<Void> publish(String channel, Map<String, Object> data) {
    final CompletableFuture<Void> publishCompleter = new CompletableFuture<>();

    connect(
        () -> {
          // Client $clientId queuing published message to $channel: $data
          final Message message = new Message(channel);
          message.setData(data);
          message.setClientId(clientId);
          sendMessage(
              message,
              response -> {
                if (response.isSuccessful() != null && response.isSuccessful()) {
                  publishCompleter.complete(null);
                } else {
                  final FayeClientError error = FayeClientError.parse(response.getError());
                  publishCompleter.completeExceptionally(error);
                }
              });
        });

    return publishCompleter;
  }

  private final String EVENT_MESSAGE = "message";

  private void subscribeChannel(String name, ChannelSubscription channelSubscription) {
    Channel channel;
    if (channels.containsKey(name)) {
      channel = channels.get(name);
    } else {
      channel = new Channel(name);
      channels.put(name, channel);
    }
    channel.bind(EVENT_MESSAGE, channelSubscription::call);
  }

  private boolean unsubscribeChannel(String name, ChannelSubscription channelSubscription) {
    final Channel channel = channels.get(name);
    if (channel == null) return false;
    channel.unbind(EVENT_MESSAGE, channelSubscription::call);
    try {
      if (channel.hasListeners(EVENT_MESSAGE)) {
        channels.remove(name);
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private void distributeChannelMessage(Message message) {
    final List<String> expandedChannels = Channel.expand(message.getChannel());
    if (expandedChannels == null) return;
    for (String c : expandedChannels) {
      final Channel channel = this.channels.get(c);
      if (channel != null) channel.trigger(EVENT_MESSAGE, message);
    }
  }

  private int messageId = 0;

  private String generateMessageId() {
    messageId += 1;
    if (messageId >= Math.pow(2, 32)) messageId = 0;
    return Integer.toString(messageId, 36);
  }

  private void sendMessage(Message message) {
    sendMessage(message, null);
  }

  private void sendMessage(Message message, MessageCallback onResponse) {
    final String id = generateMessageId();
    message.setId(id);
    message = messageTransformer.transformRequest(message);
    // Sending Message: $message
    if (onResponse != null) responseCallbacks.put(id, onResponse);
    try {
      final byte[] payload = Serialization.toJSON(message);
      webSocket.send(new String(payload));
    } catch (Exception ignored) {

    }
  }

  private void receiveMessage(Message message) {
    final String id = message.getId();
    MessageCallback callback = null;
    if (message.isSuccessful() != null) {
      callback = responseCallbacks.remove(id);
    }
    message = messageTransformer.transformResponse(message);
    // Received message: $message
    if (message.getAdvice() != null) handleAdvice(message.getAdvice());
    deliverMessage(message);
    if (callback != null) callback.onMessage(message);
  }

  private void handleAdvice(Advice advice) {
    this.advice = advice;
    if (Objects.equals(advice.getReconnect(), Advice.HANDSHAKE)
        && state != FayeClientState.DISCONNECTED) {
      setState(FayeClientState.UNCONNECTED);
      clientId = null;
      cycleConnection();
    }
  }

  private void deliverMessage(Message message) {
    if (message.getChannel() == null || message.getData() == null) return;
    // Client $clientId calling listeners for ${message.channel} with ${message.data}
    distributeChannelMessage(message);
  }

  private void cycleConnection() {
    if (connectRequestInProgress) {
      connectRequestInProgress = false;
      // Closed connection for $clientId
    }
    scheduleTimerTask(this::connect, advice.getInterval());
  }
}
