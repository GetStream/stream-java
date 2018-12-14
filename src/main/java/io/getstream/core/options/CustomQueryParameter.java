package io.getstream.core.options;

import io.getstream.core.http.Request;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class CustomQueryParameter implements RequestOption {
    private final String name;
    private final String value;

    public CustomQueryParameter(String name, String value) {
        checkNotNull(name, "Missing query parameter name");
        checkNotNull(value, "Missing query parameter value");
        checkArgument(!name.isEmpty(), "Query parameter name can't be empty");

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void apply(Request.Builder builder) {
        builder.addQueryParameter(name, value);
    }
}
