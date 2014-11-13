package io.getstream.client.model;

import io.getstream.client.service.StreamRepository;

public class UserFeed extends BaseFeed {

	public UserFeed() {
		super();
	}

    public UserFeed(final StreamRepository streamRepository) {
        super(Feed.USER, streamRepository);
    }
}
