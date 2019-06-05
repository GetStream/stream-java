package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.FileStorageClient;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

public class CloudFileStorageClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void uploadBytes() throws Exception {
        FileStorageClient client = Client.builder(apiKey, secret)
                .build()
                .files();

        URL result = client.upload("test.txt", "Hello World!".getBytes(StandardCharsets.UTF_8)).join();
    }

    @Test
    public void uploadFile() throws Exception {
        FileStorageClient client = Client.builder(apiKey, secret)
                .build()
                .files();

        File file = FileSystems.getDefault().getPath("data", "test.txt").toFile();
        URL result = client.upload(file).join();
    }

    @Test
    public void delete() throws Exception {
        FileStorageClient client = Client.builder(apiKey, secret)
                .build()
                .files();

        URL result = client.upload("test.txt", "Goodbye World!".getBytes(StandardCharsets.UTF_8)).join();
        client.delete(result).join();
    }
}