package io.getstream.core;

public enum Region {
  US_EAST("us-east-api"),
  TOKYO("tokyo-api"),
  DUBLIN("dublin-api"),
  SINGAPORE("singapore-api"),
  CANADA("ca-central-1");

  private final String region;

  Region(String region) {
    this.region = region;
  }

  @Override
  public String toString() {
    return region;
  }
}
