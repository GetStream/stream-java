package io.getstream.core.utils;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Read the properties carved at build time into the jar artifact.
 */
public final class Info {
    public static final String VERSION = "version";
    public static final String STREAM_JAVA_INFO = "/stream-java2.info";

    /**
     * Read the build properties carved into the jar.
     *
     * @return Build properties
     */
    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = Resources.asByteSource(Info.class.getResource(STREAM_JAVA_INFO)).openStream()) {
            properties.load(inputStream);
        } catch (IOException e) {
            //TODO: log exception
        }
        return properties;
    }
}
