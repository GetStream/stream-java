package io.getstream.cloud;

import io.getstream.core.StreamImages;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.options.Crop;
import io.getstream.core.options.Resize;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class CloudImageStorageClient {
    private final Token token;
    private final StreamImages images;

    CloudImageStorageClient(Token token, StreamImages images) {
        this.token = token;
        this.images = images;
    }

    public CompletableFuture<URL> upload(String fileName, byte[] content) throws StreamException {
        return images.upload(token, fileName, content);
    }

    public CompletableFuture<URL> upload(File content) throws StreamException {
        return images.upload(token, content);
    }

    public CompletableFuture<Void> delete(URL url) throws StreamException {
        return images.delete(token, url);
    }

    public CompletableFuture<URL> process(URL url, Crop crop) throws StreamException {
        return images.process(token, url, crop);
    }

    public CompletableFuture<URL> process(URL url, Resize resize) throws StreamException {
        return images.process(token, url, resize);
    }
}
