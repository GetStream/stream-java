package io.getstream.core.utils;

import io.getstream.core.options.ActivityMarker;
import io.getstream.core.options.EnrichmentFlags;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Pagination;

public final class DefaultOptions {
    static final int DEFAULT_LIMIT = 25;
    public static final Pagination DEFAULT_PAGINATION = new Pagination().limit(DEFAULT_LIMIT);
    public static final Filter DEFAULT_FILTER = new Filter();
    public static final ActivityMarker DEFAULT_MARKER = new ActivityMarker();
    public static final EnrichmentFlags DEFAULT_ENRICHMENT_FLAGS = new EnrichmentFlags();
    public static final int DEFAULT_ACTIVITY_COPY_LIMIT = 100;
    public static final int MAX_ACTIVITY_COPY_LIMIT = 1000;
}
