package io.getstream.core.faye.emitter;


class ListenerEntry<T> {

    public ListenerEntry(EventListener<T> listener, Integer limit) {
        this.listener = listener;
        this.limit = limit;
    }

    private Integer limit;
    private final EventListener<T> listener;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public EventListener<T> getListener() {
        return listener;
    }
}
