package io.getstream.core.utils;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import io.getstream.core.models.Activity;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;

import static io.getstream.core.utils.Serialization.fromJSON;
import static io.getstream.core.utils.Serialization.toJSON;
import static org.junit.Assert.assertEquals;

//XXX: disabled due to Java 1.8 requirement by Junit QuickCkeck
//@RunWith(JUnitQuickcheck.class)
public class SerializationPropertiesTest {
    @Property
    public void fixedPoint(@From(ActivityGenerator.class) Activity activity) throws Exception {
        assertEquals(activity, fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class));
        assertEquals(activity, fromJSON(new ByteArrayInputStream(toJSON(fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class))), Activity.class));
    }
}

