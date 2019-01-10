package io.getstream.core.utils;

import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Info {
    private static final String STREAM_JAVA_INFO = "/stream-java2.info";

    public static final String VERSION = "version";

    private Info() { /* nothing to see here */ }

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
