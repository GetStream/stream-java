package io.getstream.client.model.bean;

import javax.xml.datatype.Duration;

public class StreamResponse<T> {

    private String duration;

    private T results;

    public T getResults() {
        return results;
    }

    public String getDuration() {
        return duration;
    }
}
