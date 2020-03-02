package io.getstream.core.options;

import static com.google.common.base.Preconditions.checkArgument;

import io.getstream.core.http.Request;

public final class Offset implements RequestOption {
  private final int offset;

  public Offset(int value) {
    checkArgument(value >= 0, "offset can't be negative");
    offset = value;
  }

  @Override
  public void apply(Request.Builder builder) {
    if (offset > 0) {
      builder.addQueryParameter("offset", Integer.toString(offset));
    }
  }
}
