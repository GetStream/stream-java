package io.getstream.client;

import io.getstream.core.options.Crop;
import io.getstream.core.options.Resize;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;

public class ImageStorageClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void uploadBytes() throws Exception {
        ImageStorageClient client = Client.builder(apiKey, secret)
                .build()
                .images();

        byte[] bytes = new byte[]{
                0x42, 0x4d, 0x1e, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00,
                0x1a, 0x00, 0x00, 0x00, 0x0c,
                0x00, 0x00, 0x00, 0x01, 0x00,
                0x01, 0x00, 0x01, 0x00, 0x18,
                0x00, 0x00, 0x00, (byte) 0xff, 0x00
        };
        URL result = client.upload("test.bmp", bytes).join();
    }

    @Test
    public void uploadFile() throws Exception {
        ImageStorageClient client = Client.builder(apiKey, secret)
                .build()
                .images();

        File file = FileSystems.getDefault().getPath("data", "test.jpg").toFile();
        URL result = client.upload(file).join();
    }

    @Test
    public void delete() throws Exception {
        ImageStorageClient client = Client.builder(apiKey, secret)
                .build()
                .images();

        byte[] bytes = new byte[]{
                0x42, 0x4d, 0x1e, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00,
                0x1a, 0x00, 0x00, 0x00, 0x0c,
                0x00, 0x00, 0x00, 0x01, 0x00,
                0x01, 0x00, 0x01, 0x00, 0x18,
                0x00, 0x00, 0x00, (byte) 0xff, 0x00
        };
        URL result = client.upload("test.bmp", bytes).join();
        client.delete(result).join();
    }


    @Test
    public void processResize() throws Exception {
        ImageStorageClient client = Client.builder(apiKey, secret)
                .build()
                .images();

        URL image = new URL("https://stream-cloud-uploads.imgix.net/images/16794/d9b5be8c-1e50-472e-9fc8-5aa0257f33d5.test.jpg?s=f46268b5bc2783c6c675d161a3ee1dd0");
        URL result = client.process(image, new Resize(32, 32, Resize.Type.SCALE)).join();
    }

    @Test
    public void processCrop() throws Exception {
        ImageStorageClient client = Client.builder(apiKey, secret)
                .build()
                .images();

        URL image = new URL("https://stream-cloud-uploads.imgix.net/images/16794/d9b5be8c-1e50-472e-9fc8-5aa0257f33d5.test.jpg?s=f46268b5bc2783c6c675d161a3ee1dd0");
        URL result = client.process(image, new Crop(32, 32, Crop.Type.TOP, Crop.Type.LEFT)).join();
    }
}