package io.getstream.cloud;

import io.getstream.core.StreamFiles;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public final class CloudFileStorageClient {
    private final Token token;
    private final StreamFiles files;

    CloudFileStorageClient(Token token, StreamFiles files) {
        this.token = token;
        this.files = files;
    }

    public CompletableFuture<URL> upload(String fileName, byte[] content) throws StreamException {
        return files.upload(token, fileName, content);
    }

    public CompletableFuture<URL> upload(File content) throws StreamException {
        return files.upload(token, content);
    }

    public CompletableFuture<Void> delete(URL url) throws StreamException {
        return files.delete(token, url);
    }
}
