package io.getstream.client.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Deserialize date discarding microseconds. It should be enabled only on Jdk7
 */
public class PersonalizedDateDeserializer extends JsonDeserializer<Date> {

    private final static Pattern MICROSECONDS_PATTERN = Pattern.compile("^(.*)\\.[0-9]{6}$");
    private static final String DEFAULT_TIMEZONE = "UTC";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String sourceTimestamp = node.asText();

        if ( MICROSECONDS_PATTERN.matcher(sourceTimestamp).matches() ) {
            sourceTimestamp = sourceTimestamp.substring(0, sourceTimestamp.length()-3);
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
            return dateFormat.parse(sourceTimestamp);
        } catch (ParseException e) {
            throw new JsonParseException("Cannot parse input date " + sourceTimestamp, jsonParser.getCurrentLocation());
        }
    }
}
