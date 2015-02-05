package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.bean.FeedFollow;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.util.List;

public interface Feed {
	void follow(String targetFeedId) throws IOException, StreamClientException;

	void unfollow(String targetFeedId) throws IOException, StreamClientException;

	List<FeedFollow> getFollowers() throws IOException, StreamClientException;

	List<FeedFollow> getFollowers(FeedFilter filter) throws IOException, StreamClientException;

	List<FeedFollow> getFollowing() throws IOException, StreamClientException;

	List<FeedFollow> getFollowing(FeedFilter filter) throws IOException, StreamClientException;
}
