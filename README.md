# Official Java SDK for [Stream Feeds](https://getstream.io/activity-feeds/)

[![build](https://github.com/GetStream/stream-java/workflows/build/badge.svg)](https://github.com/GetStream/stream-java/actions)

<p align="center">
    <img src="./assets/logo.svg" width="50%" height="50%">
</p>
<p align="center">
    Official Java API client for Stream Feeds, a web service for building scalable newsfeeds and activity streams.
    <br />
    <a href="https://getstream.io/activity-feeds/docs/?language=java"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://getstream.github.io/stream-java/">JavaDoc</a>
    ·
    <a href="https://github.com/GetStream/stream-java/issues">Report Bug</a>
    ·
    <a href="https://github.com/GetStream/stream-java/issues">Request Feature</a>
</p>

## 📝 About Stream

You can sign up for a Stream account at our [Get Started](https://getstream.io/activity-feeds/docs/java/?language=java) page.

You can use this library to access feeds API endpoints server-side.

For the client-side integrations (web and mobile) have a look at the JavaScript, iOS and Android SDK libraries ([docs](https://getstream.io/activity-feeds/)).

> 💡 Note: this is a library for the **Feeds** product. The Chat SDKs can be found [here](https://getstream.io/chat/docs/).

## ⚙️ Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.getstream.client</groupId>
    <artifactId>stream-java</artifactId>
    <version>$stream_version</version>
</dependency>
```

or in your `build.gradle`:

```gradle
implementation 'io.getstream.client:stream-java:$stream_version'
```

In case you want to download the artifact and put it manually into your project,
you can download it from [here](https://github.com/GetStream/stream-java/releases).

Snapshots of the development version are available in [Sonatype](https://oss.sonatype.org/content/repositories/snapshots/io/getstream/client/) snapshots repository.

> 💡This API Client project requires Java SE 7.

## 🙋 FAQ

1. Is Android supported?

Yes. Use `client` for your backend and use `CloudClient` for your mobile application.

2. Cannot construct an instance of `io.getstream.core.models.*`, a model object in android. What is the problem?

If you're using proguard, ensure having following: `-keep class io.getstream.core.models.** { *; }`

Additionally, we're using Jackson JSON processor and see [their definitions](https://github.com/FasterXML/jackson-docs/wiki/JacksonOnAndroid) too unless you're already using it.

## 📚 Full documentation

Documentation for this Java client are available at the [Stream website](https://getstream.io/docs/?language=java).

For examples have a look [here](./example/Example.java).

Docs are available on [GetStream.io](https://getstream.io/docs/?language=java).

JavaDoc is available [here](https://getstream.github.io/stream-java/).

## 🧪 Building & Testing

Run `gradlew test` to execute integration tests


## ✍️ Contributing

We welcome code changes that improve this library or fix a problem, please make sure to follow all best practices and add tests if applicable before submitting a Pull Request on Github. We are very happy to merge your code in the official repository. Make sure to sign our [Contributor License Agreement (CLA)](https://docs.google.com/forms/d/e/1FAIpQLScFKsKkAJI7mhCr7K9rEIOpqIDThrWxuvxnwUq2XkHyG154vQ/viewform) first. See our [license file](./LICENSE) for more details.

## 🧑‍💻 We are hiring!

We've recently closed a [$38 million Series B funding round](https://techcrunch.com/2021/03/04/stream-raises-38m-as-its-chat-and-activity-feed-apis-power-communications-for-1b-users/) and we keep actively growing.
Our APIs are used by more than a billion end-users, and you'll have a chance to make a huge impact on the product within a team of the strongest engineers all over the world.

Check out our current openings and apply via [Stream's website](https://getstream.io/team/#jobs).
