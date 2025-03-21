package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchDeleteReactionsRequest {

    private final List<String> ids;
    private final Boolean SoftDelete;


    public BatchDeleteReactionsRequest(@JsonProperty("ids") List<String> ids) {
        this.ids = ids;
        SoftDelete = null;
    }
    public BatchDeleteReactionsRequest(@JsonProperty("ids") List<String> ids, @JsonProperty("soft_delete") Boolean softDelete) {
        this.ids = ids;
        SoftDelete = softDelete;
    }

    public List<String> getIds() {
        return ids;
    }
}