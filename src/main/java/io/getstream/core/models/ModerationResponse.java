package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationResponse {
  private String Status;
  private String RecommendedAction;
  private APIError APIError;
  private String OriginFeed;
  private String LatestModeratorAction;

  // Default constructor
  public ModerationResponse() {}

  // Constructor with parameters
  @JsonCreator
  public ModerationResponse(
      @JsonProperty("status") String status,
      @JsonProperty("recommended_action") String recommendedAction,
      @JsonProperty("api_error") APIError apiError,
      @JsonProperty("origin_feed") String originFeed,
      @JsonProperty("latest_moderator_action") String latestModeratorAction) {
    this.Status = status;
    this.RecommendedAction = recommendedAction;
    this.APIError = apiError;
    this.OriginFeed = originFeed;
    this.LatestModeratorAction = latestModeratorAction;
  }

  // Getters
  public String getStatus() {
    return Status;
  }

  public String getRecommendedAction() {
    return RecommendedAction;
  }

  public APIError getAPIError() {
    return APIError;
  }

  public String getOriginFeed() {
    return OriginFeed;
  }

  public String getLatestModeratorAction() {
    return LatestModeratorAction;
  }
}
