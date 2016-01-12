package io.getstream.client.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InfoUtilTest {

    @Test
    public void shouldGetProperties() {
        assertThat(InfoUtil.getProperties().getProperty("version"), is("1.0"));
    }

}