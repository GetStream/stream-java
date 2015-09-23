package io.getstream.client.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class InfoUtilTest {

	@Test
	public void shouldGetProperties() {
		assertThat(InfoUtil.getProperties().getProperty(InfoUtil.VERSION), is(any(String.class)));
	}
}