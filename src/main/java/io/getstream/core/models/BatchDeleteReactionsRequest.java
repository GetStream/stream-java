package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchDeleteReactionsRequest {

    private final List<String> ids;

    public BatchDeleteReactionsRequest(@JsonProperty("ids") List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }
}