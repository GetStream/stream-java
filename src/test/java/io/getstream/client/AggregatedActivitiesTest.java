package io.getstream.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.io.Resources;
import io.getstream.client.model.activities.AggregatedActivity;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.beans.StreamResponse;
import org.junit.Test;

import java.io.IOException;

public class AggregatedActivitiesTest {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    @Test
    public void testAggregated() throws IOException {
        OBJECT_MAPPER.readValue(Resources.getResource("aggregated.json"),
                                       //new TypeReference<StreamResponse<AggregatedActivity<SimpleActivity>>>(){}
                                       OBJECT_MAPPER.getTypeFactory().constructParametricType(
                                            StreamResponse.class,
                                            OBJECT_MAPPER.getTypeFactory().constructParametricType(AggregatedActivity.class, SimpleActivity.class)));
    }

}
