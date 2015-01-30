package io.getstream.client.model.activities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.bean.FeedFilter;
import io.getstream.client.model.feeds.BaseFeed;
import io.getstream.client.service.StreamRepositoryRestImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Using this class is preferable in case of large set of data. It will produce a lower memory footprint
 * since the activities are fetched and deserialized on the fly.
 * @param <T>
 */
public class ActivitiesEnumerator<T extends BaseActivity> implements Enumeration<T> {

	private final StreamRepositoryRestImpl streamRepository;
	private InputStream fetchedContentStream;
	private ObjectMapper objectMapper;

	public ActivitiesEnumerator(final StreamRepositoryRestImpl streamRepository, final ObjectMapper objectMapper) {
		this.streamRepository = streamRepository;
		this.objectMapper = objectMapper;
	}

	public void execute(BaseFeed feed, FeedFilter filter, Class<T> type) throws IOException, StreamClientException {
	}

	@Override
	public boolean hasMoreElements() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public T nextElement() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
