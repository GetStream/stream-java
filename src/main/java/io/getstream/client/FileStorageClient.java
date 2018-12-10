package io.getstream.client;

import io.getstream.core.StreamFiles;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.utils.Auth.TokenAction;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static io.getstream.core.utils.Auth.buildFilesToken;

public class FileStorageClient {
    private final String secret;
    private final StreamFiles files;

    FileStorageClient(String secret, StreamFiles files) {
        this.secret = secret;
        this.files = files;
    }

    public CompletableFuture<URL> upload(String fileName, byte[] content) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.WRITE);
        return files.upload(token, fileName, content);
    }

    public CompletableFuture<URL> upload(File content) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.WRITE);
        return files.upload(token, content);
    }

    public CompletableFuture<Void> delete(URL url) throws StreamException {
        final Token token = buildFilesToken(secret, TokenAction.DELETE);
        return files.delete(token, url);
    }
}
