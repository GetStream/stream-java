package io.getstream.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfoTest {
    @Test
    public void shouldGetProperties() {
        assertNotNull(Info.getProperties().getProperty("version"));
    }
}