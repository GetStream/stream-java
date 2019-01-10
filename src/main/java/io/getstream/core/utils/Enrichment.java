package io.getstream.core.utils;

public final class Enrichment {
    private Enrichment() { /* nothing to see here */ }

    public static String createCollectionReference(String collection, String id) {
        return String.format("SO:%s:%s", collection, id);
    }

    public static String createUserReference(String id) {
        return String.format("SU:%s", id);
    }
}
