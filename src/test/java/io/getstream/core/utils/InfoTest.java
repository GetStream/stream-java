package io.getstream.core.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InfoTest {
  @Test
  public void shouldGetProperties() {
    assertNotNull(Info.getProperties().getProperty("version"));
  }
}
