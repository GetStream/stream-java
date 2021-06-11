package io.getstream.core.faye.emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventEmitter<T> {

    private final Map<String, LinkedList<ListenerEntry<T>>> events = new HashMap<>();

    private ErrorListener errorListener;

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    private boolean mounted = true;

    public boolean isMounted() {
        return mounted;
    }

    private void assertMounted() {
        assert isMounted() : "Tried to use " + this.getClass().getSimpleName() + " after `dispose` was called. Consider checking `isMounted`";
    }

    public void emit(String event, T data) {
        assertMounted();
        final LinkedList<ListenerEntry<T>> listeners = events.get(event);
        if (listeners == null) return;
        boolean didThrow = false;
        final List<ListenerEntry<T>> removables = new ArrayList<>();
        for (ListenerEntry<T> entry : listeners) {
            try {
                entry.getListener().onData(data);
                Integer limit = entry.getLimit();
                if (limit != null) {
                    if (limit > 0) {
                        limit -= 1;
                        entry.setLimit(limit);
                    }
                    if (limit == 0) {
                        removables.add(entry);
                    }
                }
            } catch (Exception e) {
                didThrow = true;
                if (errorListener != null) {
                    errorListener.onError(e);
                }
            }
        }
        for (ListenerEntry<T> entry : removables) {
            listeners.remove(entry);
        }
        if (didThrow) throw new Error();
    }

    public void on(String event, EventListener<T> listener) {
        addListener(event, listener);
    }

    public void on(String event, EventListener<T> listener, int limit) {
        addListener(event, listener, limit);
    }

    public void addListener(String event, EventListener<T> listener) {
        _addListener(event, listener, null);
    }

    public void addListener(String event, EventListener<T> listener, int limit) {
        _addListener(event, listener, limit);
    }

    void _addListener(String event, EventListener<T> listener, Integer limit) {
        assertMounted();
        final ListenerEntry<T> entry = new ListenerEntry<T>(listener, limit);
        LinkedList<ListenerEntry<T>> listeners = events.get(event);
        if (listeners == null) listeners = new LinkedList<>();
        listeners.add(entry);
        events.put(event, listeners);
    }

    public void off(String event) {
        assertMounted();
        events.put(event, new LinkedList<>());
    }

    public void removeListener(String event, EventListener<T> listener) {
        assertMounted();
        final LinkedList<ListenerEntry<T>> listeners = events.get(event);
        if (listeners == null) return;
        listeners.removeIf(curr -> curr.getListener() == listener);
    }

    public void removeAllListeners() {
        assertMounted();
        events.clear();
    }

    public boolean hasListeners(String event) throws Exception {
        assertMounted();
        final LinkedList<ListenerEntry<T>> listeners = events.get(event);
        if (listeners == null) {
            throw new Exception("Event not available");
        }
        return !listeners.isEmpty();
    }

    public void dispose() {
        assertMounted();
        events.values().forEach(LinkedList::clear);
        mounted = false;
    }

}
