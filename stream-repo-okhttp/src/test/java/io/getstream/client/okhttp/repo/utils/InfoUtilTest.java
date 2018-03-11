package io.getstream.client.okhttp.repo.utils;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import io.getstream.client.util.InfoUtil;

public class InfoUtilTest {

	@Test
	public void shouldGetProperties() {
		assertThat(InfoUtil.getProperties().getProperty(InfoUtil.VERSION), is(any(String.class)));
	}
}