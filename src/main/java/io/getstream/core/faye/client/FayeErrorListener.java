package io.getstream.core.faye.client;

import okhttp3.Response;

public interface FayeErrorListener {
    void onError(Throwable t, Response response);
}
