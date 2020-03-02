package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import io.getstream.core.http.Request;
import java.util.Set;

public final class ActivityMarker implements RequestOption {
  private boolean allRead = false;
  private boolean allSeen = false;
  private Set<String> readIDs = Sets.newHashSet();
  private Set<String> seenIDs = Sets.newHashSet();

  public ActivityMarker allRead() {
    allRead = true;
    return this;
  }

  public ActivityMarker allSeen() {
    allSeen = true;
    return this;
  }

  public ActivityMarker read(String... activityIDs) {
    if (!allRead && activityIDs != null) {
      readIDs = Sets.union(readIDs, ImmutableSet.copyOf(activityIDs));
    }
    return this;
  }

  public ActivityMarker read(Iterable<String> activityIDs) {
    return read(Iterables.toArray(checkNotNull(activityIDs), String.class));
  }

  public ActivityMarker seen(String... activityIDs) {
    if (!allSeen && activityIDs != null)
      seenIDs = Sets.union(seenIDs, ImmutableSet.copyOf(activityIDs));
    return this;
  }

  public ActivityMarker seen(Iterable<String> activityIDs) {
    return seen(Iterables.toArray(checkNotNull(activityIDs), String.class));
  }

  @Override
  public void apply(Request.Builder builder) {
    if (allRead) {
      builder.addQueryParameter("mark_read", "true");
    } else if (!readIDs.isEmpty()) {
      builder.addQueryParameter("mark_read", String.join(",", readIDs));
    }

    if (allSeen) {
      builder.addQueryParameter("mark_seen", "true");
    } else if (!seenIDs.isEmpty()) {
      builder.addQueryParameter("mark_seen", String.join(",", seenIDs));
    }
  }
}
