/**

 Copyright (c) 2015, Alessandro Pieri
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.

 */
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
 * into a nested array, e.g.:<br/>
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
 * <br/>
 * This deserializer parses the nested array and returns collapsed entries like those:<br/>
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
