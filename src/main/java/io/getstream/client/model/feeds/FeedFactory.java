package io.getstream.client.model.feeds;

/**
 * Factory class to create new feed.
 */
public interface FeedFactory {

	/**
	 * Create new feed.
	 * @param feedSlug feed slug.
	 * @param id feed id.
	 * @return
	 */
    Feed createFeed(String feedSlug, String id);
}
