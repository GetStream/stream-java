package io.getstream.client.model.activities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

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
