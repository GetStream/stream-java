package io.getstream.client;

import io.getstream.core.models.Data;
import io.getstream.core.models.ProfileData;
import org.junit.Test;

public class UserTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    public void get() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "get-user";
        User user = client.user(userID);
        user.getOrCreate().join();
        Data result = user.get().join();
    }

    @Test
    public void delete() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "delete-user";
        User user = client.user(userID);
        user.getOrCreate().join();
        user.delete().join();
    }

    @Test
    public void getOrCreate() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "get-or-create-user";
        User user = client.user(userID);
        try {
            user.delete().join();
        } catch (Exception ignored) {
            //XXX: do nothing
            System.out.println(ignored);
        }
        Data result = user.getOrCreate().join();
    }

    @Test
    public void create() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "create-user";
        User user = client.user(userID);
        try {
            user.delete().join();
        } catch (Exception ignored) {
            //XXX: do nothing
            System.out.println(ignored);
        }
        Data result = user.create().join();
    }

    @Test
    public void update() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "update-user";
        User user = client.user(userID);
        user.getOrCreate().join();
        Data result = user.update(new Data().set("key", "value")).join();
    }

    @Test
    public void profile() throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        String userID = "new-profile-user";
        User user = client.user(userID);
        user.getOrCreate().join();
        ProfileData result = user.profile().join();
    }
}