package io.getstream.core.http;

import java.util.concurrent.CompletableFuture;

public abstract class HTTPClient {
    public static Request.Builder requestBuilder() {
        return Request.builder();
    }

    public abstract <T> T getImplementation();
    public abstract CompletableFuture<Response> execute(Request request);
}
