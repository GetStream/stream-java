package io.getstream.client.model.feeds;

public enum Feed {

    USER (UserFeed.class, "user"),
    FLAT (FlatFeed.class, "flat"),
    AGGREGATED (AggregatedFeed.class, "aggregated"),
    NOTIFICATION (NotificationFeed.class, "notification");

    private final String path;
    private final Class<? extends BaseFeed> classType;

    private Feed(Class<? extends BaseFeed> classType, final String path) {
        this.classType = classType;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path;
    }

    public Class<? extends BaseFeed> getClassType() {
        return classType;
    }
}
