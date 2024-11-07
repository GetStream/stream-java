package io.getstream.core;

import static io.getstream.core.utils.Request.buildGet;
import static io.getstream.core.utils.Serialization.deserialize;

import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Token;
import io.getstream.core.models.ExportIDsResponse;
import static io.getstream.core.utils.Routes.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java8.util.concurrent.CompletableFuture;
import java8.util.concurrent.CompletionException;

public class ExportIDs {
    private final String key;
    private final URL baseURL;
    private final HTTPClient httpClient;

    public ExportIDs(String key, URL baseURL, HTTPClient httpClient) {
        this.key = key;
        this.baseURL = baseURL;
        this.httpClient = httpClient;
    }

    public CompletableFuture<ExportIDsResponse> exportUserActivities(Token token, String userId) throws StreamException {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID can't be null or empty");
        }

        try {
//            final URL url = buildExportIDsURL(baseURL, userId);
            final URL url = buildExportIDsURL(new URL("https://oregon-api.stream-io-api.com"), userId);//$$ need to deploy proxy
            io.getstream.core.http.Request request = buildGet(url, key, token);
            return httpClient
                    .execute(request)
                    .thenApply(
                            response -> {
                                try {
                                    return deserialize(response, ExportIDsResponse.class);
                                } catch (StreamException | IOException e) {
                                    throw new CompletionException(e);
                                }
                            });
        } catch (MalformedURLException | URISyntaxException e) {
            throw new StreamException(e);
        }
    }
}