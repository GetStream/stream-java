package io.getstream.client.model.activities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Jackson's custom deserializer is designed to handle responses containing signed recipients.
 * As response of adding activities, GetStream.io returns signed recipients contained
 * into a nested array, e.g.:<br>
 * <pre>
 * "to": [
 *   [
 *     "user:1",
 *     "6mQhuzQ79e0rZ17bSq1CCxXoRac"
 *   ],
 *   [
 *     "user:2",
 *     "6mQhuzQ79e0rZ17bSq1CCxJu788"
 *   ]
 * ]
 * </pre>
 * <br>
 * This deserializer parses the nested array and returns collapsed entries like those:<br>
 * <pre>
 * "to": [
 *   "user:1 6mQhuzQ79e0rZ17bSq1CCxXoRac",
 *   "user:2 6mQhuzQ79e0rZ17bSq1CCxJu788"
 * ]
 * </pre>
 */
public class ActivitySignedRecipientDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String value = null;
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            value = String.format("%s %s", jsonParser.nextTextValue(), jsonParser.nextTextValue());
            jsonParser.nextToken();
        } else {
            value = jsonParser.getValueAsString();
        }
        return value;
    }
}
