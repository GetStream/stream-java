package io.getstream.core.utils;

import io.getstream.core.options.*;

public final class DefaultOptions {
    private DefaultOptions() { /* nothing to see here */ }

    public static final Limit DEFAULT_LIMIT = new Limit(25);
    public static final Offset DEFAULT_OFFSET = new Offset(0);
    public static final Filter DEFAULT_FILTER = new Filter();
    public static final ActivityMarker DEFAULT_MARKER = new ActivityMarker();
    public static final EnrichmentFlags DEFAULT_ENRICHMENT_FLAGS = new EnrichmentFlags();
    public static final int DEFAULT_ACTIVITY_COPY_LIMIT = 100;
    public static final int MAX_ACTIVITY_COPY_LIMIT = 1000;
}
