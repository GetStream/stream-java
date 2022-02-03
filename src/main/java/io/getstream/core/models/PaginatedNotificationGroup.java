package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginatedNotificationGroup<T> extends Paginated<NotificationGroup<T>> {
  private final int unread;
  private final int unseen;

  @JsonCreator
  public PaginatedNotificationGroup(
      @JsonProperty("next") String next,
      @JsonProperty("results") List<NotificationGroup<T>> results,
      @JsonProperty("duration") String duration,
      @JsonProperty("unread") int unread,
      @JsonProperty("unseen") int unseen) {
    super(next, results, duration);

    this.unread = unread;
    this.unseen = unseen;
  }

  public int getUnread() {
    return this.unread;
  }

  public int getUnseen() {
    return this.unseen;
  }
}
