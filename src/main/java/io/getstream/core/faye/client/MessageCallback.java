package io.getstream.core.faye.client;

import io.getstream.core.faye.Message;

interface MessageCallback {
    void onMessage(Message message);
}
