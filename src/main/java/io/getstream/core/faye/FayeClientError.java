package io.getstream.core.faye;

import com.google.common.base.MoreObjects;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FayeClientError extends Throwable {
  private final Integer code;
  private final List<String> params;
  private final String errorMessage;

  public FayeClientError(Integer code, List<String> params, String errorMessage) {
    this.code = code;
    this.params = params;
    this.errorMessage = errorMessage;
  }

  public static FayeClientError parse(String errorMessage) {
    if (errorMessage == null) errorMessage = "";
    if (!Grammar.ERROR.matcher(errorMessage).matches()) {
      return new FayeClientError(null, null, errorMessage);
    }

    final List<String> parts = Arrays.asList(errorMessage.split(":"));
    final Integer code = Integer.parseInt(parts.get(0));
    final List<String> params = Arrays.asList(parts.get(1).split(","));
    final String message = parts.get(2);

    return new FayeClientError(code, params, message);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FayeClientError that = (FayeClientError) o;
    return Objects.equals(code, that.code)
        && Objects.equals(params, that.params)
        && Objects.equals(errorMessage, that.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, params, errorMessage);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("code", this.code)
        .add("params", this.params)
        .add("errorMessage", this.errorMessage)
        .toString();
  }
}
