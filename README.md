stream-java
===========
[![Build Status](https://travis-ci.org/GetStream/stream-java.svg?branch=master)](https://travis-ci.org/GetStream/stream-java)

[stream-java](https://github.com/GetStream/stream-java) is a Java client for [Stream](https://getstream.io/).

You can sign up for a Stream account at https://getstream.io/get_started.

The Stream's Java client come in two different flavours, you should decide which one to drag into your project.
Those two implementations differ according to the underlying library used to handle HTTP connections:

- *stream-repo-apache* uses Apache HttpClient and we recommend it for backend applications. Apache HttpClient is a mature, reliable and rock-solid HTTP library.
- *stream-repo-okhttp* uses Square's OkHttp which is lightweight, powerful and mobile-oriented HTTP library. We recommend it for mobile application.

### Installation

If you decide to go for the *Apache HttpClient* implementation, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.getstream.client</groupId>
    <artifactId>stream-repo-apache</artifactId>
    <version>2.0.1</version>
</dependency>
```

or in your build.gradle:

```gradle
compile 'io.getstream.client:stream-repo-apache:2.0.1'
```

Instead, if you opted for the *OkHttp* implementation please add it to your pom.xml

```xml
<dependency>
    <groupId>io.getstream.client</groupId>
    <artifactId>stream-repo-okhttp</artifactId>
    <version>2.0.1</version>
</dependency>
```

or in your build.gradle:

```gradle
compile 'io.getstream.client:stream-repo-okhttp:2.0.1'
```

In case you want to download the artifact and put it manually into your project,
you can download it from [here](https://github.com/GetStream/stream-java/releases).

Snapshots of the development version are available in [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/io/getstream/client/) snapshots repository.

#### JDK / JVM version requirements

This API Client project requires Java SE 8.

See the [Travis configuration](.travis.yml) for details of how it is built, tested and packaged.

### Full documentation

Documentation for this Java client are available at the [Stream website](https://getstream.io/docs/?language=java).

### Usage

```java
/**
 * Instantiate a new client to connect to us east API endpoint
 * Find your API keys here https://getstream.io/dashboard/
 **/

StreamClient streamClient = new StreamClientImpl(new ClientConfiguration(), "<API_KEY>", "<API_SECRET>");
```

#### Create a new Feed

```java
/* Instantiate a feed object */
Feed feed = streamClient.newFeed("user", "1");
```

#### Working with Activities

```java
/* Create an activity service */
FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);

/* Get activities from 5 to 10 (using offset pagination) */
FeedFilter filter = new FeedFilter.Builder().withLimit(5).withOffset(5).build();
List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();

/* Filter on an id less than the given UUID */
aid = "e561de8f-00f1-11e4-b400-0cc47a024be0";
FeedFilter filter = new FeedFilter.Builder().withIdLowerThan(aid).withLimit(5).build();
List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();

/* Create a new activity */
SimpleActivity activity = new SimpleActivity();
activity.setActor("user:1");
activity.setObject("tweet:1");
activity.setVerb("tweet");
activity.setForeignId("tweet:1");
SimpleActivity response = flatActivityService.addActivity(activity);

/* Remove an activity by its id */
feed.deleteActivity("e561de8f-00f1-11e4-b400-0cc47a024be0");

/* Remove activities by their foreign_id */
feed.deleteActivityByForeignId("tweet:1");
```

In case you want to add a single activity to multiple feeds, you can use the batch feature _addToMany_:

```java
/* Batch adding activities to many feeds */
flatActivityService.addActivityToMany(ImmutableList.<String>of("user:1", "user:2").asList(), myActivity);
```

The API client allows you to send activities with custom field as well, you can find a
complete example [here](https://github.com/GetStream/stream-java/blob/master/stream-repo-apache/src/test/java/io/getstream/client/apache/example/mixtype/MixedType.java)

#### Follow and Unfollow

```java
/* Follow another feed */
feed.follow(flat", "42");

/* Stop following another feed */
feed.unfollow(flat", "42");

/* Retrieve first 10 followers of a feed */
FeedFilter filter = new FeedFilter.Builder().withLimit(10).build();
List<FeedFollow> followingPaged = feed.getFollowing(filter);

/* Retrieve the first 10 followed feeds */
FeedFilter filter = new FeedFilter.Builder().withLimit(10).build();
List<FeedFollow> followingPaged = feed.getFollowing(filter);
```

In case you want to send to Stream a long list of following relationships you can use the batch feature _followMany_:

```java
/* Batch following many feeds */
FollowMany followMany = new FollowMany.Builder()
    .add("user:1", "user:2")
    .add("user:1", "user:3")
    .add("user:1", "user:4")
    .add("user:2", "user:3")
    .build();
feed.followMany(followMany);

```

#### Client token

In order to generate a token for client side usage (e.g. JS client), you can use the following code:

```java
/* Generating tokens for client side usage */
String token = feed.getToken();
```

#### Further references

For more examples have a look [here](https://github.com/GetStream/stream-java/tree/master/stream-repo-apache/src/test/java/io/getstream/client/apache/example).

Docs are available on [GetStream.io](http://getstream.io/docs/).

Javadocs are available [here](https://getstream.github.io/stream-java/).

### Credits & Contributors

This project was originally contributed by [Alessandro Pieri](sirio7g), prior to him joining Stream as an employee.

We continue to welcome pull requests from community members.

### Copyright and License Information

Copyright (c) 2016-2017 Stream.io Inc, and individual contributors. All rights reserved.

See the file "LICENSE" for information on the history of this software, terms & conditions for usage, and a DISCLAIMER OF ALL WARRANTIES.
