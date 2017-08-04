package io.getstream.client.repo;

import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.PersonalizedActivity;
import io.getstream.client.model.beans.MetaResponse;
import io.getstream.client.model.feeds.PersonalizedFeed;
import io.getstream.client.model.filters.FeedFilter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Provide low-level access to the GetStream.io Personalized REST API.
 */
public interface StreamPersonalizedRepository {

    /**
     * Read a personalized feed.
     * The response is deserialized by using Jackson, therefore Jackson's annotations and custom deserializers are honored.
     * @param feed the feed you want to read from
     * @param type Since the personalized feed can be heavily customized, we need to know which class to use to perform deserialized.
     * @param <T> Type of the class to use to perform deserialization.
     * @param filter Filter out the results.
     * @return Personalized feed
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends PersonalizedActivity> List<T> get(PersonalizedFeed feed, Class<T> type, FeedFilter filter) throws IOException, StreamClientException;

    /**
     * "Certain objects such as user profiles, product details etc. aren't part of the feeds. They can however be useful for personalization."
     * Use this method to sync this data to Stream.
     * The meta payload is serialized by using Jackson, therefore Jackson's annotations and custom serializers are honored.
     * @param feed The feed you want to enrich with meta-data
     * @param metaPayload The meta object can be customized by the customer. The method accepts any Serializable object.
     * @return Short response about the call.
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    MetaResponse addMeta(PersonalizedFeed feed, Serializable metaPayload) throws IOException, StreamClientException;

    /**
     * Read the taste/ endpoint.
     * The response is deserialized by using Jackson, therefore Jackson's annotations and custom deserializers are honored.
     * This endpoint is not provided by default and is activated to selected customer.
     * @param feed the feed you want to read from
     * @param type Since the personalized feed can be heavily customized, we need to know which class to use to perform deserialized.
     * @param <T> Type of the class to use to perform deserialization.
     * @return Taste results
     * @throws IOException in case of network/socket exceptions
     * @throws StreamClientException in case of functional or server-side exception
     */
    <T extends Serializable> List<T> getInterest(PersonalizedFeed feed, Class<T> type) throws IOException, StreamClientException;
}
