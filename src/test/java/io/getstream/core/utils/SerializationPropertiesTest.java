package io.getstream.core.utils;

import static io.getstream.core.utils.Serialization.fromJSON;
import static io.getstream.core.utils.Serialization.toJSON;
import static org.junit.Assert.assertEquals;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import io.getstream.core.models.Activity;
import java.io.ByteArrayInputStream;

// XXX: disabled due to Java 1.8 requirement by Junit QuickCkeck
// @RunWith(JUnitQuickcheck.class)
public class SerializationPropertiesTest {
  @Property
  public void fixedPoint(@From(ActivityGenerator.class) Activity activity) throws Exception {
    assertEquals(activity, fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class));
    assertEquals(
        activity,
        fromJSON(
            new ByteArrayInputStream(
                toJSON(fromJSON(new ByteArrayInputStream(toJSON(activity)), Activity.class))),
            Activity.class));
  }
}
