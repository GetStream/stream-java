package io.getstream.core;

public enum LookupKind {
    ACTIVITY("activity_id"),
    USER("user_id");

    private final String kind;

    LookupKind(String kind) {
        this.kind = kind;
    }

    public String getKind() {
        return kind;
    }
}
