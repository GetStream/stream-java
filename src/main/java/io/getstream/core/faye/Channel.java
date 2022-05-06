package io.getstream.core.faye;

import com.google.common.base.MoreObjects;
import io.getstream.core.faye.emitter.EventEmitter;
import io.getstream.core.faye.emitter.EventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Channel {

  public static final String HANDSHAKE = "/meta/handshake";
  public static final String CONNECT = "/meta/connect";
  public static final String DISCONNECT = "/meta/disconnect";
  public static final String SUBSCRIBE = "/meta/subscribe";
  public static final String UNSUBSCRIBE = "/meta/unsubscribe";

  private final String name;

  public Channel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  final EventEmitter<Message> eventEmitter = new EventEmitter<>();

  public void bind(String event, EventListener<Message> listener) {
    eventEmitter.on(event, listener);
  }

  public void unbind(String event, EventListener<Message> listener) {
    eventEmitter.removeListener(event, listener);
  }

  public void trigger(String event, Message message) {
    eventEmitter.emit(event, message);
  }

  public boolean hasListeners(String event) throws Exception {
    return eventEmitter.hasListeners(event);
  }

  public static List<String> expand(String name) {
    final List<String> channels = new ArrayList<>(Arrays.asList("/**", name));
    final List<String> segments = parse(name);

    if (segments == null) return null;

    List<String> copy = new ArrayList<>(segments);
    copy.add(copy.size() - 1, "*");
    channels.add(unparse(copy));

    for (int i = 1; i < segments.size(); i++) {
      copy = segments.subList(0, i);
      copy.add("**");
      channels.add(unparse(copy));
    }

    return channels;
  }

  public static boolean isValid(String name) {
    return Grammar.CHANNEL_NAME.matcher(name).matches()
        || Grammar.CHANNEL_PATTERN.matcher(name).matches();
  }

  public static List<String> parse(String name) {
    if (!isValid(name)) return null;
    final String[] splits = name.split("/");
    return Arrays.asList(splits).subList(1, splits.length);
  }

  public static String unparse(List<String> segments) {
    final String joinedSegments = String.join("/", segments);
    return "/" + joinedSegments;
  }

  public static Boolean isMeta(String name) {
    final List<String> segments = parse(name);
    if (segments == null) return null;
    return segments.get(0).equals("meta");
  }

  public static Boolean isService(String name) {
    final List<String> segments = parse(name);
    if (segments == null) return null;
    return segments.get(0).equals("service");
  }

  public static Boolean isSubscribable(String name) {
    if (!isValid(name)) return null;
    final Boolean isMeta = isMeta(name);
    final Boolean isService = isService(name);
    if (isMeta == null || isService == null) return null;
    return !isMeta && !isService;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Channel that = (Channel) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).omitNullValues().add("name", this.name).toString();
  }
}
