package io.getstream.core.http;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

public final class Token {
  private final String token;

  public Token(String token) {
    checkNotNull(token, "Token can't be null");
    checkArgument(!token.isEmpty(), "Token can't be null");

    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Token token1 = (Token) o;
    return Objects.equals(token, token1.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    return token;
  }
}
