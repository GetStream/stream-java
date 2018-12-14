package io.getstream.client;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FileStorageClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void uploadBytes() {
        URL[] result = new URL[1];
        assertDoesNotThrow(() -> {
            FileStorageClient client = Client.builder(apiKey, secret)
                    .build()
                    .files();

            result[0] = client.upload("test.txt", "Hello World!".getBytes(StandardCharsets.UTF_8)).join();
        });
    }

    @Test
    void uploadFile() {
        URL[] result = new URL[1];
        assertDoesNotThrow(() -> {
            FileStorageClient client = Client.builder(apiKey, secret)
                    .build()
                    .files();

            File file = FileSystems.getDefault().getPath("data", "test.txt").toFile();
            result[0] = client.upload(file).join();
        });
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> {
            FileStorageClient client = Client.builder(apiKey, secret)
                    .build()
                    .files();

            URL result = client.upload("test.txt", "Goodbye World!".getBytes(StandardCharsets.UTF_8)).join();
            client.delete(result).join();
        });
    }
}