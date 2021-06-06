package io.getstream.core.faye.subscription;

import java.util.Map;

public interface ChannelDataCallback {
    void onData(Map<String, Object> data);
}