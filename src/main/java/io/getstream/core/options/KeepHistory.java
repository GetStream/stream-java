package io.getstream.core.options;

import io.getstream.core.http.Request;

public final class KeepHistory implements RequestOption {
  private final boolean keepHistory;

  public KeepHistory(io.getstream.core.KeepHistory keepHistory) {
    this.keepHistory = keepHistory.getFlag();
  }

  public boolean getFlag() {
    return keepHistory;
  }

  @Override
  public void apply(Request.Builder builder) {
    builder.addQueryParameter("keep_history", Boolean.toString(keepHistory));
  }
}
