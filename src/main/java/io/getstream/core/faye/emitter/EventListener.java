package io.getstream.core.faye.emitter;

public interface EventListener<T> {
    void onData(T data);
}
