package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.getstream.core.http.Request;

public final class Ranking implements RequestOption {
  private final String ranking;

  public Ranking(String ranking) {
    checkNotNull(ranking, "Ranking can't be empty");
    checkArgument(!ranking.isEmpty(), "Ranking can't be empty");
    this.ranking = ranking;
  }

  public String getRanking() {
    return ranking;
  }

  @Override
  public void apply(Request.Builder builder) {
    builder.addQueryParameter("ranking", ranking);
  }
}
