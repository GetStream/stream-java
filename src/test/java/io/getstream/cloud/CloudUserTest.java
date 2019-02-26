package io.getstream.cloud;

import io.getstream.client.Client;
import io.getstream.client.User;
import io.getstream.core.exceptions.StreamException;
import io.getstream.core.http.Token;
import io.getstream.core.models.Data;
import io.getstream.core.models.ProfileData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CloudUserTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";
    private static final String userID = "db07b4a3-8f48-41f7-950c-b228364496e2";
    private static final Token token = buildToken();

    private static Token buildToken() {
        try {
            return Client.builder(apiKey, secret).build().frontendToken(userID);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @BeforeEach
    void setup() {
        try {
            Client.builder(apiKey, secret)
                    .build()
                    .user(userID)
                    .getOrCreate()
                    .join();
        } catch (StreamException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void get() {
        Data[] result = new Data[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            result[0] = user.get().join();
        });
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            user.delete().join();
        });
    }

    @Test
    void getOrCreate() {
        Data[] result = new Data[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            user.delete().join();
            result[0] = user.getOrCreate().join();
        });
    }

    @Test
    void create() {
        Data[] result = new Data[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            user.delete().join();
            result[0] = user.create().join();
        });
    }

    @Test
    void update() {
        Data[] result = new Data[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            result[0] = user.update(new Data()
                    .set("key", "value")
                    .set("null", null)).join();
        });
    }

    @Test
    void profile() {
        ProfileData[] result = new ProfileData[1];
        assertDoesNotThrow(() -> {
            CloudClient client = CloudClient.builder(apiKey, token, userID).build();

            CloudUser user = client.user(userID);
            result[0] = user.profile().join();
        });
    }
}