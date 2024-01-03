# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [3.8.2](https://github.com/GetStream/stream-java/compare/v3.8.1...v3.8.2) (2024-01-03)

### [3.8.1](https://github.com/GetStream/stream-java/compare/v3.8.0...v3.8.1) (2023-11-17)

## [3.8.0](https://github.com/GetStream/stream-java/compare/v3.7.0...v3.8.0) (2023-10-23)
* Added support to force refresh
* Added support for soft deletions

## [3.7.0](https://github.com/GetStream/stream-java/compare/v3.6.2...v3.7.0) (2023-08-16)


### Features

* add capability in batch client to get enrichment activities to use enrichment flags ([cfcc86a](https://github.com/GetStream/stream-java/commit/cfcc86ae3b62fd16cbf733912e7b484a91bb7d8b))


### Bug Fixes

* spotless ([dad14c7](https://github.com/GetStream/stream-java/commit/dad14c7abedd6ca6d9cef7ea4c1a0133cb648e72))

### [3.6.2](https://github.com/GetStream/stream-java/compare/v3.5.0...v3.6.2) (2023-01-26)


### Features

* **faye:** add websocket client ([#110](https://github.com/GetStream/stream-java/issues/110)) ([c0b29e5](https://github.com/GetStream/stream-java/commit/c0b29e51708e424f44686e20d9b2b426da661b4c))

### [3.6.1](https://github.com/GetStream/stream-java/compare/v3.5.0...v3.6.1) (2023-01-26)


### Features

* **faye:** add websocket client ([#110](https://github.com/GetStream/stream-java/issues/110)) ([c0b29e5](https://github.com/GetStream/stream-java/commit/c0b29e51708e424f44686e20d9b2b426da661b4c))

## [3.6.0](https://github.com/GetStream/stream-java/compare/v3.5.0...v3.6.0) (2022-05-26)


### Features

* **faye:** add websocket client ([#110](https://github.com/GetStream/stream-java/issues/110)) ([c0b29e5](https://github.com/GetStream/stream-java/commit/c0b29e51708e424f44686e20d9b2b426da661b4c))

## [3.5.0](https://github.com/GetStream/stream-java/compare/v3.4.1...v3.5.0) (2022-04-26)


### Features

* **stats:** add followstats ([#108](https://github.com/GetStream/stream-java/issues/108)) ([56afa90](https://github.com/GetStream/stream-java/commit/56afa9098d6d21eac5e6c0b75975b32c6684358b))

## [3.4.1] - 2022-03-22

- Fix unmarshal of empty custom data

## [3.4.0] - 2022-02-10

- Add unread/unseed counts into notification feed payloads

## [3.3.0] - 2022-01-07

- Relax id checks for custom data in collections
- Add withOwnChildren support into reaction filtering
- Move to GitHub actions and add release automation
- Add proguard notes into readme and bump some deps due to security notices
- Add changelog
