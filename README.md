stream-java
===========
[![Build Status](https://travis-ci.org/GetStream/stream-java.svg?branch=master)](https://travis-ci.org/GetStream/stream-java)

stream-java is a Java client for [Stream](https://getstream.io/).


### Installation

Download the latest JAR or grab via Maven:

```xml
<dependency>
  <groupId></groupId>
  <artifactId></artifactId>
  <version></version>
</dependency>
```

or Gradle:

```
compile 'groupId:artifactId:version'
```

Snapshots of the development version are available in Sonatype's snapshots repository.

### Usage

```java
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.feeds.Feed;

// Instantiate a new client to connect to us east API endpoint
// Find your API keys here https://getstream.io/dashboard/

ClientConfiguration streamConfig = new ClientConfiguration().setRegion(StreamRegion.US_EAST);
StreamClient streamClient = new StreamClientImpl(streamConfig, 'API_KEY', 'API_SECRET');

// Instantiate a feed object
Feed feed = streamClient.newFeed("user", "1");

// Create an activity service
FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);

// Get activities from 5 to 10 (using offset pagination)
FeedFilter filter = new FeedFilter.Builder().withLimit(5).withOffset(5).build();
List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();

// Filter on an id less than the given UUID
aid = "e561de8f-00f1-11e4-b400-0cc47a024be0";
FeedFilter filter = new FeedFilter.Builder().withIdLowerThan(aid).withLimit(5).build();
List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();

// Create a new activity
SimpleActivity activity = new SimpleActivity();
activity.setActor("user:1");
activity.setObject("tweet:1");
activity.setVerb("tweet");
activity.setForeignId("tweet:1");
SimpleActivity response = flatActivityService.addActivity(activity);
```

The API client allows you to send activities with custom field as well, you can find a complete example [here](https://github.com/GetStream/stream-java/blob/master/stream-repo-apache/src/test/java/io/getstream/client/example/mixtype/MixedType.java)

```java
// Remove an activity by its id
feed.deleteActivity("e561de8f-00f1-11e4-b400-0cc47a024be0");

// Remove activities by their foreign_id
feed.deleteActivityByForeignId("tweet:1");

// Follow another feed
feed.follow(flat", "42");

// Stop following another feed
feed.unfollow(flat", "42");

// Batch adding activities
// This is not supported yet

// Batch following many feeds
// This is not supported yet

// Add an activity and push it to other feeds too using the `to` field
// This is not supported yet

// Remove a feed and its content
// This is not supported yet

// Generating tokens for client side usage
String token = feed.getToken();

// Javascript client side feed initialization
// user1 = client.feed('user', '1', '{{ token }}');

// Retrieve first 10 followers of a feed
FeedFilter filter = new FeedFilter.Builder().withLimit(10).build();
List<FeedFollow> followingPaged = feed.getFollowing(filter);

// Retrieve the first 10 followed feeds
FeedFilter filter = new FeedFilter.Builder().withLimit(10).build();
List<FeedFollow> followingPaged = feed.getFollowing(filter);

// Check if specific feeds are followed
// This is not supported yet

```

Docs are available on [GetStream.io](http://getstream.io/docs/).
