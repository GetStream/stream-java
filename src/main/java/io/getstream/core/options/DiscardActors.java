package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkNotNull;

import io.getstream.core.http.Request;
import java.util.List;

public final class DiscardActors implements RequestOption {
  private final String actors;
  private final String separator;

  public DiscardActors(String... actors) {
    this(actors, ",");
  }

  public DiscardActors(List<String> actors) {
    this(actors, ",");
  }

  public DiscardActors(String[] actors, String separator) {
    checkNotNull(actors, "Actors list cannot be null");
    this.actors = String.join(separator != null ? separator : ",", actors);
    this.separator = separator;
  }

  public DiscardActors(List<String> actors, String separator) {
    checkNotNull(actors, "Actors list cannot be null");
    this.actors = String.join(separator != null ? separator : ",", actors);
    this.separator = separator;
  }

  @Override
  public void apply(Request.Builder builder) {
    if (actors != null && !actors.isEmpty()) {
      builder.addQueryParameter("discard_actors", actors);
      if (separator != null && !separator.equals(",")) {
        builder.addQueryParameter("discard_actors_sep", separator);
      }
    }
  }
}
