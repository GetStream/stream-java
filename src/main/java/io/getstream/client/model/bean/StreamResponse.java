package io.getstream.client.model.bean;

import java.util.List;

public class StreamResponse<T> {

    private String duration;

    private List<T> results;

	private String next;

    public List<T> getResults() {
        return results;
    }

    public String getDuration() {
        return duration;
    }

	public String getNext() {
		return next;
	}
}
