package io.getstream.core.utils;

import com.google.common.base.Joiner;
import io.getstream.core.models.FeedID;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class Routes {
  private static final String basePath = "/api/v1.0/";
  private static final String analyticsPath = "/analytics/v1.0/";
  private static final String personalizationPath = "/personalization/v1.0/";
  private static final String analyticsSubdomain = "analytics";
  private static final String personalizationSubdomain = "personalization";
  private static final String activitiesPath = "activities/";
  private static final String activityUpdatePath = "activity/";
  private static final String addToManyPath = "feed/add_to_many/";
  private static final String followManyPath = "follow_many/";
  private static final String unfollowManyPath = "unfollow_many/";
  private static final String moderationFlagPath = "moderation/flag/";
  private static final String collectionsPath = "collections/";
  private static final String filesPath = "files/";
  private static final String imagesPath = "images/";
  private static final String openGraphPath = "og/";
  private static final String reactionsPath = "reaction/";
  private static final String reactionsBatchPath = "reaction/get_many/";
  private static final String toTargetUpdatePath = "/activity_to_targets/";
  private static final String usersPath = "user/";
  private static final String followStatsPath = "stats/follow/";

  private static final String exportIDsPath = "data_privacy/export_ids/";
  private static final String deleteActivitiesPath = "data_privacy/delete_activities/";
  private static final String deleteReactionsPath = "data_privacy/delete_reactions/";

  private Routes() {
    /* nothing to see here */
  }

  public static URL buildFeedURL(URL baseURL, FeedID feed, String path)
      throws MalformedURLException {
    return new URL(baseURL, basePath + feedPath(feed) + path);
  }

  public static URL buildEnrichedFeedURL(URL baseURL, FeedID feed, String path)
      throws MalformedURLException {
    return new URL(baseURL, basePath + enrichedFeedPath(feed) + path);
  }

  public static URL buildToTargetUpdateURL(URL baseURL, FeedID feed) throws MalformedURLException {
    return new URL(baseURL, basePath + feedTargetsPath(feed) + toTargetUpdatePath);
  }

  public static URL buildActivitiesURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + activitiesPath);
  }

  public static URL buildEnrichedActivitiesURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + enriched(activitiesPath));
  }

  public static URL buildCollectionsURL(URL baseURL, String path) throws MalformedURLException {
    return new URL(baseURL, basePath + collectionsPath + path);
  }

  public static URL buildReactionsURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + reactionsPath);
  }

  public static URL buildReactionsURL(URL baseURL, String path) throws MalformedURLException {
    return new URL(baseURL, basePath + reactionsPath + path);
  }

 public static URL buildGetReactionsBatchURL(URL baseURL) throws MalformedURLException {
  return new URL(baseURL, basePath + reactionsBatchPath);
}

  public static URL buildUsersURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + usersPath);
  }

  public static URL buildUsersURL(URL baseURL, String path) throws MalformedURLException {
    return new URL(baseURL, basePath + usersPath + path);
  }

  public static URL buildBatchCollectionsURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + collectionsPath);
  }

  public static URL buildOpenGraphURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + openGraphPath);
  }

  public static URL buildFilesURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + filesPath);
  }

  public static URL buildImagesURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + imagesPath);
  }

  public static URL buildPersonalizationURL(URL baseURL, String path) throws MalformedURLException {
    return buildSubdomainPath(baseURL, personalizationSubdomain, personalizationPath, path);
  }

  public static URL buildAnalyticsURL(URL baseURL, String path) throws MalformedURLException {
    return buildSubdomainPath(baseURL, analyticsSubdomain, analyticsPath, path);
  }

  public static URL buildActivityUpdateURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + activityUpdatePath);
  }

  public static URL buildAddToManyURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + addToManyPath);
  }

  public static URL buildFollowManyURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + followManyPath);
  }

  public static URL buildUnfollowManyURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + unfollowManyPath);
  }

  public static URL buildModerationFlagURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + moderationFlagPath);
  }

  public static URL buildExportIDsURL(URL baseURL, String userID) throws MalformedURLException {
    return new URL(baseURL, basePath + exportIDsPath+userID);
  }

  public static URL deleteActivitiesURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + deleteActivitiesPath);
  }

  public static URL deleteReactionsURL(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + deleteReactionsPath);
  }

  public static URL followStatsPath(URL baseURL) throws MalformedURLException {
    return new URL(baseURL, basePath + followStatsPath);
  }

  private static URL buildSubdomainPath(URL baseURL, String subdomain, String apiPath, String path)
      throws MalformedURLException {
    try {
      URI baseURI = baseURL.toURI();
      String[] parts = baseURI.getHost().split("\\.");
      // assume at minimum host will have name and TLD parts
      if (parts.length > 2) {
        // replace first subdomain
        parts[0] = subdomain;
      }

      return new URI(
              baseURI.getScheme(),
              baseURI.getUserInfo(),
              Joiner.on(".").join(parts),
              baseURI.getPort(),
              baseURI.getPath() + apiPath + path,
              baseURI.getQuery(),
              null)
          .toURL();
    } catch (URISyntaxException e) {
      throw new MalformedURLException(e.getReason());
    }
  }

  private static String feedPath(FeedID feed) {
    return String.format("feed/%s/%s", feed.getSlug(), feed.getUserID());
  }

  private static String feedTargetsPath(FeedID feed) {
    return String.format("feed_targets/%s/%s", feed.getSlug(), feed.getUserID());
  }

  private static String enrichedFeedPath(FeedID feed) {
    return String.format("enrich/feed/%s/%s", feed.getSlug(), feed.getUserID());
  }

  private static String enriched(String url) {
    return String.format("enrich/%s", url);
  }
}
