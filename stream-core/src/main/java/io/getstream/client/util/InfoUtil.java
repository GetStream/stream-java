package io.getstream.client.util;

import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read the properties carved at build time into the jar artifact.
 */
public class InfoUtil {

	private static final Logger LOG = LoggerFactory.getLogger(InfoUtil.class);

	public static final String VERSION = "version";
	public static final String STREAM_JAVA_INFO = "/stream-java.info";

	/**
	 * Read the build properties carved into the jar.
	 * @return Build properties
	 */
	public static Properties getProperties() {
		Properties properties = new Properties();
		try (InputStream inputStream = Resources.asByteSource(InfoUtil.class.getResource(STREAM_JAVA_INFO)).openStream()) {
			properties.load(inputStream);
		} catch (IOException e) {
			LOG.error("Failed to read the properties file " + STREAM_JAVA_INFO + "from the jar", e);
		}
		return properties;
	}
}
