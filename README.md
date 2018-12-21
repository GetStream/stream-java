stream-java
===========
[stream-java](https://github.com/GetStream/stream-java) is a Java client for [Stream](https://getstream.io/).

You can sign up for a Stream account at https://getstream.io/get_started.

### Installation

Add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.getstream.client</groupId>
    <artifactId>stream-java</artifactId>
    <version>3.0.1</version>
</dependency>
```

or in your build.gradle:

```gradle
compile 'io.getstream.client:stream-java:3.0.1'
```

In case you want to download the artifact and put it manually into your project,
you can download it from [here](https://github.com/GetStream/stream-java/releases).

Snapshots of the development version are available in [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/io/getstream/client/) snapshots repository.

#### JDK / JVM version requirements

This API Client project requires Java SE 8.

### Full documentation

Documentation for this Java client are available at the [Stream website](https://getstream.io/docs/?language=java).

For examples have a look [here](https://github.com/GetStream/stream-java/tree/master/example/Example.java).

Docs are available on [GetStream.io](http://getstream.io/docs/).

Javadocs are available [here](https://getstream.github.io/stream-java/).

### Building & Testing

Run `gradle wrapper --gradle-version 5.0` to generate gradle wrapper files

Run `gradle test` to execute integration tests

### Credits & Contributors

Project is maintained by [Max Klyga](nekuromento).

This project was originally contributed by [Alessandro Pieri](sirio7g).

We continue to welcome pull requests from community members.

### Copyright and License Information

Copyright (c) 2016-2018 Stream.io Inc, and individual contributors. All rights reserved.

See the file "LICENSE" for information on the history of this software, terms & conditions for usage, and a DISCLAIMER OF ALL WARRANTIES.
