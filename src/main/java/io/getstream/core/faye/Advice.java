package io.getstream.core.faye;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Advice {
    private final String reconnect;
    private final Integer interval;
    private final Integer timeout;

    public static final String NONE = "none";
    public static final String HANDSHAKE = "handshake";
    public static final String RETRY = "retry";

    // for deserialization
    public Advice() {
        reconnect = null;
        interval = null;
        timeout = null;
    }

    public Advice(String reconnect, Integer interval, Integer timeout) {
        this.reconnect = reconnect;
        this.interval = interval;
        this.timeout = timeout;
    }

    public String getReconnect() {
        return reconnect;
    }

    public Integer getInterval() {
        return interval;
    }

    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advice that = (Advice) o;
        return Objects.equals(reconnect, that.reconnect)
                && Objects.equals(interval, that.interval)
                && Objects.equals(timeout, that.timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reconnect, interval, timeout);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("reconnect", this.reconnect)
                .add("interval", this.interval)
                .add("timeout", this.timeout)
                .toString();
    }
}
