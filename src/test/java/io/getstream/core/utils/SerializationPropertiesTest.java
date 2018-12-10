package io.getstream.core.utils;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.getstream.core.models.Activity;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;

import static io.getstream.core.utils.Serialization.fromJSON;
import static io.getstream.core.utils.Serialization.toJSON;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class SerializationPropertiesTest {
    @Property
    public void fixedPoint(@From(ActivityGenerator.class) Activity activity) {
        assertDoesNotThrow(() -> {
            assertEquals(activity, fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class));
            assertEquals(activity, fromJSON(new ByteArrayInputStream(toJSON(fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class))), Activity.class));
        });
    }
}

