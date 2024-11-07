package io.getstream.core;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.BatchDeleteActivitiesRequest;
import java8.util.concurrent.CompletableFuture;
import static io.getstream.core.utils.Routes.*;
import static io.getstream.core.utils.Request.buildPost;
import static io.getstream.core.utils.Serialization.toJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class BatchDeleteActivities {
    private final String key;
    private final URL baseURL;
    private final HTTPClient httpClient;

    public BatchDeleteActivities(String key, URL baseURL, HTTPClient httpClient) {
        this.key = key;
        this.baseURL = baseURL;
        this.httpClient = httpClient;
    }

    public CompletableFuture<Object> deleteActivities(Token token, BatchDeleteActivitiesRequest request) throws StreamException {
        try {
//            final URL url = deleteActivitiesURL(baseURL);
            final URL url = deleteActivitiesURL(new URL("https://oregon-api.stream-io-api.com"));//$$ need to deploy proxy

            final byte[] payload = toJSON(request);
            io.getstream.core.http.Request httpRequest = buildPost(url, key, token, payload);
            return httpClient.execute(httpRequest).thenApply(response -> null);
        } catch (Exception e) {
            throw new StreamException(e);
        }
    }
}