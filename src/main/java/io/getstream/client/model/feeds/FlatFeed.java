package io.getstream.client.model.feeds;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.BaseActivity;
import io.getstream.client.service.StreamRepositoryRestImpl;

import java.io.IOException;
import java.util.List;

public class FlatFeed<T extends BaseActivity> extends BaseFeed {
    public FlatFeed(StreamRepositoryRestImpl streamRepository, String feedSlug, String id) {
        super(Feed.FLAT, streamRepository, feedSlug, id);
    }

	public void addActivity(T activity) throws IOException, StreamClientException {
		streamRepository.addActivity(this, activity);
	}

	public List<T> getActivities() throws IOException, StreamClientException {
		return (List<T>) streamRepository.getActivities(this);
	}
}
