package io.getstream.core.models.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import io.getstream.core.models.Data;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public final class DataDeserializer extends StdDeserializer<Data> {
    public DataDeserializer() {
        super(Data.class);
    }

    @Override
    public Data deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode node = parser.readValueAsTree();
        if (node.getNodeType() != JsonNodeType.OBJECT) {
            return new Data(node.asText());
        }
        Data data = new Data(node.get("id").asText());
        for (Map.Entry<String, JsonNode> field : iterate(node.fields())) {
            if (field.getKey().equals("id"))
                continue;
            if (field.getKey().equals("data")) {
                data.from(field.getValue());
            } else {
                data.set(field.getKey(), parser.getCodec().treeToValue(field.getValue(), Object.class));
            }
        }
        return data;
    }

    private static <T> Iterable<T> iterate(Iterator<T> iterator) {
        return () -> iterator;
    }
}
