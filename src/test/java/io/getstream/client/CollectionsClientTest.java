package io.getstream.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableMap;
import io.getstream.core.models.CollectionData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JsonInclude(JsonInclude.Include.NON_NULL)
class CustomType {
    public String id = "";
    public String foreign_id;
    public String collection;
    public String key;
    public String updated_at;
    public String created_at;
    public String duration;
    public Map<String, Object> extra;
}

class CollectionsClientTest {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    @Test
    void addCustom() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CustomType data = new CustomType();
            data.key = "value";
            data.extra = ImmutableMap.of("test", "test");
            client.addCustom("add_custom_collection", data).join();
        });
    }

    @Test
    void add() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            client.add("add_collection", data).join();
        });
    }

    @Test
    void updateCustom() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            try {
                data = client.add("update_custom_collection", data).join();
            } catch (Exception ignored) {
                //XXX: do nothing
                System.out.println(ignored);
            }
            CustomType otherData = new CustomType();
            otherData.id = data.getID();
            otherData.key = "another_value";
            otherData.extra = ImmutableMap.of("test", "another_test");
            client.updateCustom("update_custom_collection", otherData).join();
        });
    }

    @Test
    void update() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            try {
                data = client.add("update_collection", data).join();
            } catch (Exception ignored) {
                //XXX: do nothing
                System.out.println(ignored);
            }
            CollectionData otherData = new CollectionData(data.getID())
                    .set("key", "other_value")
                    .set("extra", ImmutableMap.of("test", "other_test"));
            client.update("update_collection", otherData).join();
        });
    }

    @Test
    void upsertManyCustom() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            client.upsertManyCustom("upsert_many_custom_collection", new Object() {
                public final String id = "id_5";
                public final String key = "value";
                public final Map<String, Object> extra = ImmutableMap.of("test", "test");
            }).join();
        });
    }

    @Test
    void upsertMany() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData("id_6")
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            CollectionData custom = CollectionData.buildFrom(new Object() {
                public final String id = "id_7";
                public final String key = "value";
                public final Map<String, Object> extra = ImmutableMap.of("test", "test");
            });
            client.upsertMany("upsert_many_collection", data, custom).join();
        });
    }

    @Test
    void getCustom() {
        CustomType[] result = new CustomType[1];
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            try {
                data = client.add("get_custom_collection", data).join();
            } catch (Exception ignored) {
                //XXX: do nothing
                System.out.println(ignored);
            }

            result[0] = client.getCustom(CustomType.class, "get_custom_collection", data.getID()).join();
        });
    }

    @Test
    void get() {
        CollectionData[] result = new CollectionData[1];
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            try {
                data = client.add("get_collection", data).join();
            } catch (Exception ignored) {
                //XXX: do nothing
                System.out.println(ignored);
            }

            result[0] = client.get("get_collection", data.getID()).join();
        });
    }

    @Test
    void getManyCustom() {
        List<CustomType>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData("id_10")
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            client.upsertMany("get_many_custom_collection", data).join();

            result[0] = client.getManyCustom(CustomType.class, "get_many_custom_collection", "id_10").join();
        });
    }

    @Test
    void getMany() {
        List<CollectionData>[] result = new List[1];
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData("id_11")
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            client.upsertMany("get_many_custom_collection", data).join();
            result[0] = client.getMany("get_many_collection", "id_11").join();
        });
    }

    @Test
    void delete() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            CollectionData data = new CollectionData()
                    .set("key", "value")
                    .set("extra", ImmutableMap.of("test", "test"));
            try {
                data = client.add("delete_collection", data).join();
            } catch (Exception ignored) {
                //XXX: do nothing
                System.out.println(ignored);
            }

            client.delete("delete_collection", data.getID()).join();
        });
    }

    @Test
    void deleteMany() {
        assertDoesNotThrow(() -> {
            CollectionsClient client = Client.builder(apiKey, secret)
                    .build()
                    .collections();

            client.deleteMany("delete_many_collection", "id_13").join();
        });
    }
}