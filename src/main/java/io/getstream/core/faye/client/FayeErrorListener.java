package io.getstream.core.faye.client;

import okhttp3.Response;

public interface FayeErrorListener {
    void onError(Throwable throwable, Response response);
}
