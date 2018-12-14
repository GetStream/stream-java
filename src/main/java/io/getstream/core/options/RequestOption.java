package io.getstream.core.options;

import io.getstream.core.http.Request;

public interface RequestOption {
    void apply(Request.Builder builder);
}
