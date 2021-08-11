package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.FileStorageClient;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import org.junit.Test;

public class CloudFileStorageClientTest {
  private static final String apiKey = System.getenv("STREAM_KEY") != null ? System.getenv("STREAM_KEY")
      : System.getProperty("STREAM_KEY");
  private static final String secret = System.getenv("STREAM_SECRET") != null ? System.getenv("STREAM_SECRET")
      : System.getProperty("STREAM_SECRET");

  @Test
  public void uploadBytes() throws Exception {
    FileStorageClient client = Client.builder(apiKey, secret).build().files();

    URL result = client.upload("test.txt", "Hello World!".getBytes(StandardCharsets.UTF_8)).join();
  }

  @Test
  public void uploadFile() throws Exception {
    FileStorageClient client = Client.builder(apiKey, secret).build().files();

    File file = FileSystems.getDefault().getPath("data", "test.txt").toFile();
    URL result = client.upload(file).join();
  }

  @Test
  public void delete() throws Exception {
    FileStorageClient client = Client.builder(apiKey, secret).build().files();

    URL result = client.upload("test.txt", "Goodbye World!".getBytes(StandardCharsets.UTF_8)).join();
    client.delete(result).join();
  }
}
