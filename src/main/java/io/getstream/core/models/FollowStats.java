package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class FollowStats {
  @JsonProperty(value = "followers")
  private FollowStat followers;

  @JsonProperty(value = "following")
  private FollowStat following;

  public FollowStat getFollowers() {
    return followers;
  }

  public FollowStat getFollowing() {
    return following;
  }

  public class FollowStat {
    @JsonProperty(value = "count")
    private int count;

    @JsonProperty(value = "feed")
    private String feed;

    public int getCount() {
      return count;
    }

    public String getFeed() {
      return feed;
    }
  }
}
