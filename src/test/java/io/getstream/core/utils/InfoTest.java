package io.getstream.core.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InfoTest {
    @Test
    public void shouldGetProperties() {
        assertNotNull(Info.getProperties().getProperty("version"));
    }
}