package io.getstream.client;

import io.getstream.core.StreamImages;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.options.Crop;
import io.getstream.core.options.Resize;
import io.getstream.core.utils.Auth.TokenAction;
import java8.util.concurrent.CompletableFuture;

import java.io.File;
import java.net.URL;

import static io.getstream.core.utils.Auth.buildFilesToken;

public final class ImageStorageClient {
    private final String secret;
    private final StreamImages images;

    ImageStorageClient(String secret, StreamImages images) {
        this.secret = secret;
        this.images = images;
    }

    public CompletableFuture<URL> upload(String fileName, byte[] content) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.WRITE);
        return images.upload(token, fileName, content);
    }

    public CompletableFuture<URL> upload(File content) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.WRITE);
        return images.upload(token, content);
    }

    public CompletableFuture<Void> delete(URL url) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.DELETE);
        return images.delete(token, url);
    }

    public CompletableFuture<URL> process(URL url, Crop crop) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.READ);
        return images.process(token, url, crop);
    }

    public CompletableFuture<URL> process(URL url, Resize resize) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.READ);
        return images.process(token, url, resize);
    }
}
