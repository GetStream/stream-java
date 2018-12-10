package io.getstream.core.utils;

public final class Enrichment {
    public static String createReference(String collection, String id) {
        return String.format("SO:%s:%s", collection, id);
    }

    public static String createUserReference(String id) {
        return createReference("user", id);
    }
}
