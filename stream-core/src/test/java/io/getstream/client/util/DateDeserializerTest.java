package io.getstream.client.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateDeserializerTest {

    @Test
    public void shouldParseTheDate() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MyDateBean myDateBean = objectMapper.readValue("{ \"time\": \"2016-01-12T08:20:59.000Z\" }", MyDateBean.class);
        GregorianCalendar expectedDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        expectedDate.set(2016,0,12,8,20,59);
        expectedDate.set(GregorianCalendar.MILLISECOND, 0);
        assertThat(myDateBean.getTime(), is(expectedDate.getTime()));
    }

    static class MyDateBean {
        @JsonDeserialize(using = DateDeserializer.class)
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.S")
        protected Date time;

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }
}