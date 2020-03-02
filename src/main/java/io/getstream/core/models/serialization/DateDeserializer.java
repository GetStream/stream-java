package io.getstream.core.models.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateDeserializer extends StdDeserializer<Date> {
  private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private static final int allowedLength = pattern.length() - 2; // don't include 2 single quotes

  public DateDeserializer() {
    super(Date.class);
  }

  @Override
  public Date deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    formatter.setLenient(false);

    String date = parser.getValueAsString();
    // trim if date includes microseconds
    if (date.length() > allowedLength) {
      date = date.substring(0, allowedLength);
    }
    try {
      return formatter.parse(date);
    } catch (ParseException e) {
      throw context.weirdStringException(
          parser.getValueAsString(),
          Date.class,
          "Could not deserialize Date using '" + pattern + "' pattern");
    }
  }
}
