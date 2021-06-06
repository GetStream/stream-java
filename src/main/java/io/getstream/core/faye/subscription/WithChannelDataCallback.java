package io.getstream.core.faye.subscription;

import java.util.Map;

public interface WithChannelDataCallback {
    void onData(String channel, Map<String, Object> data);
}
